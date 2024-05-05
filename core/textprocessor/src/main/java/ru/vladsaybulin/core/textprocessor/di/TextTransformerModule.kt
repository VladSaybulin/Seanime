package ru.vladsaybulin.core.textprocessor.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.core.textprocessor.html.DefaultHtmlToAnnotatedTextTagTransformers
import ru.vladsaybulin.core.textprocessor.html.HtmlToAnnotatedTextTransformer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TextTransformerModule {

    @Provides
    @Singleton
    fun provideTransformerFromHtmlToAnnotatedText(): HtmlToAnnotatedTextTransformer =
        HtmlToAnnotatedTextTransformer(DefaultHtmlToAnnotatedTextTagTransformers)

}