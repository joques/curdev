package yester.lib

import play.api.libs.json._

object EnumUtils {
    def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
        def reads(json: JsValue): JsResult[E#Value] = json match {
            case JsString(s) => {
                try {
                    JsSuccess(enum.withName(s))
                } catch {
                    case _: NoSuchElementException => JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$s'")
                }
            }
            case _ => JsError("String value expected")
        }
    }
    implicit def enumWrites[E <: Enumeration]: Writes[E#Value] = new Writes[E#Value] {
        def writes(v: E#Value): JsValue = JsString(v.toString)
    }

    implicit def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = {
        Format(EnumUtils.enumReads(enum), EnumUtils.enumWrites)
    }
}

object UserProfile extends Enumeration {
    type UserProfile = Value
    val DEAN, ADEAN, VC, DVCA, REG, HOD, DHOD, CDC, DEVT, PAC, TLU, QAU, PDU, BOS, SENATE, CDE, HPGL, NGCL, CEU = Value

    implicit val userProfileFormat = EnumUtils.enumFormat(UserProfile)
}
