abstract class ResponseMessage[T](messageId: String, operationError: Option[String], operationResult: Option[T]) {}
