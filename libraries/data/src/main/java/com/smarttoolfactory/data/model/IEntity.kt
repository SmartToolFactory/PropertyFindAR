package com.smarttoolfactory.data.model

/**
 * Interface to create common behavior between entities to be able to use generics to create [Mapper]
 */
interface IEntity : Mappable

/**
 * Interface to create common behavior between DTOs to be able to use generics to create [Mapper]
 */
interface DataTransferObject : Mappable

/**
 * Marker interface to mark classes that implement, or interfaces that extend this interface
 * as convertible from one [Mappable] type to another
 */
interface Mappable
