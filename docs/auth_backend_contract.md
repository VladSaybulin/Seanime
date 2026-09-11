# Auth Backend Contract

This document describes the auth backend contract currently used by the Android client.

It reflects the implementation in:

- `core/network/.../AuthDataSource.kt`
- `core/network/.../OAuthTokenRequest.kt`
- `core/network/.../OAuthTokenBody.kt`
- `core/data/.../NetworkOAuthTokenExchangeGateway.kt`
- `core/auth/.../SessionManager.kt`

## Scope

The client currently supports only two OAuth-related backend actions:

1. exchange an authorization code for tokens
2. refresh tokens using a refresh token

`revoke` is **not supported** by the current backend contract and is not used by the client.

## Endpoint

- Method: `POST`
- Path: `/oauth/token`
- Content-Type: `application/json`

The client sends this request without an `Authorization` header.

## Request Body

The request body is serialized from `OAuthTokenRequest`.

```json
{
  "grant_type": "authorization_code | refresh_token",
  "code": "string | null",
  "refresh_token": "string | null",
  "client_id": "string",
  "client_secret": "string",
  "redirect_uri": "string | null"
}
```

## Supported Flows

### 1. Authorization code exchange

Used after the browser OAuth flow returns an authorization code.

#### Request

```json
{
  "grant_type": "authorization_code",
  "code": "<authorization_code>",
  "refresh_token": null,
  "client_id": "<client_id>",
  "client_secret": "<client_secret>",
  "redirect_uri": "<redirect_uri>"
}
```

#### Required fields

- `grant_type`
- `code`
- `client_id`
- `client_secret`
- `redirect_uri`

### 2. Refresh token flow

Used when the access token is expired or when a protected API call returns `401` and the client attempts recovery.

#### Request

```json
{
  "grant_type": "refresh_token",
  "code": null,
  "refresh_token": "<refresh_token>",
  "client_id": "<client_id>",
  "client_secret": "<client_secret>",
  "redirect_uri": null
}
```

#### Required fields

- `grant_type`
- `refresh_token`
- `client_id`
- `client_secret`

## Successful Response

On success, the backend must return HTTP `2xx` with a non-null JSON body matching `OAuthTokenBody`.

```json
{
  "access_token": "string",
  "expires_in": 3600,
  "refresh_token": "string",
  "created_at": 1726070400
}
```

### Field semantics

- `access_token`: bearer token used for authenticated API requests
- `refresh_token`: token used to obtain a new access token
- `expires_in`: token lifetime in seconds
- `created_at`: Unix timestamp in seconds when the token pair was created

### Client-side conversion

The client converts the response to internal `StoredTokens` as follows:

```text
expiresAtMs = (created_at + expires_in) * 1000
```

## Error Handling Contract

### HTTP 2xx with null body

If the backend returns a successful HTTP status but no response body, the client treats this as a fatal protocol error.

Client behavior:

- `AuthDataSource` returns `IllegalStateException("Response body is null for successful response")`
- the auth operation is treated as failed

### HTTP 4xx

Any `4xx` response from `/oauth/token` is interpreted by the client as an auth failure.

Client behavior:

- mapped to `AuthException.Unauthorized`
- during refresh, this leads to local logout
- during authorization code exchange, this leaves the session logged out

### HTTP 5xx

Any `5xx` response is treated as a backend/server failure.

Client behavior:

- mapped to `IllegalStateException("OAuth backend error: ...")`
- during refresh, current logic treats this as a failed auth recovery path and logs out locally
- network callers may also receive the exception depending on the execution path

### Transport/network failures

Examples: timeout, no internet, TLS failure, connection reset.

Client behavior:

- surfaced as `IOException`
- not converted to `AuthException.Unauthorized`
- can propagate to callers depending on where the request originated

## Session Semantics on the Client

### Login success

After a successful authorization code exchange, the client:

1. saves encrypted tokens locally
2. clears cached user id
3. sets session state to `Authenticated`

### Login failure

If authorization code exchange fails, the client remains in `LoggedOut` state.

### Refresh success

After a successful refresh, the client:

1. replaces stored tokens with the refreshed token pair
2. keeps the session in `Authenticated`

### Refresh auth failure

If refresh fails with an auth-related backend error (`4xx`), the client performs a local logout.

### Refresh unexpected failure

If refresh fails with a non-auth fatal error in the current implementation, the client may also fall back to local logout to avoid keeping an inconsistent session.

### Corrupted token storage / decrypt failure

If locally stored encrypted tokens cannot be decrypted or decoded correctly:

- `AuthTokenStore` returns `Result.failure(...)`
- `SessionManager` treats the token set as invalid
- the client performs a local logout and clears local session state

## Unsupported / Out of Scope

The following are not part of the current contract:

- token revocation endpoint / revoke action
- introspection endpoint
- device binding
- PKCE-only backend flow without `client_secret`

## Notes

- This document describes the **current implementation**, not an ideal future auth design.
- The current client still relies on `client_secret` because the available backend API requires it.
- If backend behavior changes, this file must be updated together with `AuthDataSource`, request/response models, and `SessionManager` logic.
