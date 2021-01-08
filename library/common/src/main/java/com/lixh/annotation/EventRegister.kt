package com.lixh.annotation

/**
 * 类描述：一个鉴权的注解类
 * @Target(AnnotationTarget.FUNCTION) 表示这个是对方法进行注解
 * @Retention(AnnotationRetention.RUNTIME) 表示注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventRegister(val key: String)