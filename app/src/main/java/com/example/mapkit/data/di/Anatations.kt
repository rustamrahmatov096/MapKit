package com.example.mapkit.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppCacheQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EncryptedCacheQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class XaznaServiceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EcomServiceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EcomOkHttpServiceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FileUploadServiceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FileOkHttpServiceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class XaznaOkHttpServiceQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HumoPayOkHttp