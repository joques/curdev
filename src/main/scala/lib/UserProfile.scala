package yester.lib

import play.api.libs.json.Json

object UserProfile extends Enumeration {
    type UserProfile = Value
    val DEAN, ADEAN, VC, DVCA, REG, HOD, DHOD, CDC, PAC, TLU, QAU, PDU, BOS, SENATE, CDE, HPGL, NGCL, CEU = Value

    implicit val userProfileEnum = Jsn.format[UserProfile]
}
