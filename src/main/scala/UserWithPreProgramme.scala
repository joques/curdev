import play.api.libs.json.{Json, Format}

final case class UserWithPreProgramme(userDetails: User, preProgrammeCodes: Option[List[String]])

object UserWithPreProgrammeJsonImplicits {
    implicit val userFormat: Format[User] =  UserJsonImplicits.userFmt

    implicit val uwPPFmt = Json.format[UserWithPreProgramme]
    implicit val uwPPWrites = Json.writes[UserWithPreProgramme]
    implicit val uwPPReads = Json.reads[UserWithPreProgramme]
}
