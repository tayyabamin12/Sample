package com.safidence.safidence.data.api

import com.rx2androidnetworking.Rx2AndroidNetworking
import com.safidence.safidence.data.model.*
import io.reactivex.Single
import java.io.File

class ApiServiceImpl : ApiService {

    private var baseUrl = "https://staging.safidence.com/api/"

    override fun login(nic: String, password: String): Single<ResponseLogin> {
        return Rx2AndroidNetworking.post(baseUrl.plus("login"))
            .addHeaders("Accept", "application/json")
            .addBodyParameter("nic", nic)
            .addBodyParameter("password", password)
            .build()
            .getObjectSingle(ResponseLogin::class.java)
    }

    override fun forgetPassword(nic: String): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.post(baseUrl.plus("forget-password"))
            .addHeaders("Accept", "application/json")
            .addBodyParameter("nic", nic)
            .build()
            .getObjectSingle(ResponseGeneralMessage::class.java)
    }

    override fun updatePassword(
        token: String, nic: String, oldPass: String, newPass: String
    ): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.post(baseUrl.plus("updatePassword"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addBodyParameter("current_password", oldPass)
            .addBodyParameter("new_password", newPass)
            .build()
            .getObjectSingle(ResponseGeneralMessage::class.java)
    }

    override fun logout(token: String): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.post(baseUrl.plus("logout"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getObjectSingle(ResponseGeneralMessage::class.java)
    }

    override fun getTenantData(token: String): Single<ResponseTenantData> {
        return Rx2AndroidNetworking.get(baseUrl.plus("tenant_data"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getObjectSingle(ResponseTenantData::class.java)
    }

    override fun getRequestTypes(token: String): Single<ResponseRequestTypes> {
        return Rx2AndroidNetworking.get(baseUrl.plus("request_types"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getObjectSingle(ResponseRequestTypes::class.java)
    }

    override fun getTenantUnits(token: String): Single<ResponseTenantUnits> {
        return Rx2AndroidNetworking.get(baseUrl.plus("tenant_units"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getObjectSingle(ResponseTenantUnits::class.java)
    }

    override fun getTenantRequestStatus(token: String): Single<ResponseRequestStatus> {
        return Rx2AndroidNetworking.get(baseUrl.plus("tenant_request_status"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getObjectSingle(ResponseRequestStatus::class.java)
    }

    override fun getTenantContractExpiry(
        token: String,
        unitId: Int
    ): Single<ResponseTenantContractExpiry> {
        return Rx2AndroidNetworking.get(baseUrl.plus("tenant_contract_expiryData/{unit_id}"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addPathParameter("unit_id", unitId.toString())
            .build()
            .getObjectSingle(ResponseTenantContractExpiry::class.java)
    }

    override fun tenantRequest(
        token: String,
        requestId: Int,
        subject: String,
        desc: String,
        priority: String,
        availability: String,
        phone: String,
        unitId: Int,
        media: File
    ): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.upload(baseUrl.plus("tenant_request"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addMultipartParameter("type_id", requestId.toString())
            .addMultipartParameter("subject", subject)
            .addMultipartParameter("description", desc)
            .addMultipartParameter("priority", priority)
            .addMultipartParameter("availability", availability)
            .addMultipartParameter("phone", phone)
            .addMultipartParameter("unit_id", unitId.toString())
            .addMultipartFile("media", media)
            .build()
            .getObjectSingle(ResponseGeneralMessage::class.java)
    }

    override fun getDocTypes(token: String): Single<ResponseDocTypes> {
        return Rx2AndroidNetworking.get(baseUrl.plus("document_types"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getObjectSingle(ResponseDocTypes::class.java)
    }

    override fun uploadDoc(
        token: String,
        docId: Int,
        num: String,
        country: String,
        date: String,
        media: File
    ): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.upload(baseUrl.plus("document_upload"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addMultipartParameter("document_id", docId.toString())
            .addMultipartParameter("number", num)
            .addMultipartParameter("issue_country", country)
            .addMultipartParameter("expiry_date", date)
            .addMultipartFile("image", media)
            .build()
            .getObjectSingle(ResponseGeneralMessage::class.java)
    }

    override fun getAllDocs(token: String): Single<ResponseAllDocuments> {
        return Rx2AndroidNetworking.get(baseUrl.plus("all_documents"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getObjectSingle(ResponseAllDocuments::class.java)
    }

    override fun getTenantAlerts(token: String): Single<ResponseAlerts> {
        return Rx2AndroidNetworking.get(baseUrl.plus("tenant_alerts"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getObjectSingle(ResponseAlerts::class.java)
    }

    override fun contractRequest(
        token: String,
        expiryDate: String,
        date: String,
        unitId: Int,
        isRenew: Boolean
    ): Single<ResponseGeneralMessage> {
        val response = Rx2AndroidNetworking.post(baseUrl.plus("contract_request"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addBodyParameter("current_expiry", expiryDate)
            .addBodyParameter("unit_id", unitId.toString())
        if (isRenew){
            response.addBodyParameter("request_type", "renew_contract")
            response.addBodyParameter("renew_till", date)
        }else {
            response.addBodyParameter("request_type", "end_contract")
            response.addBodyParameter("end_date", date)
        }
        return  response.build().getObjectSingle(ResponseGeneralMessage::class.java)
    }

    override fun getTenantUnitDetails(token: String, unitId: Int): Single<ResponseUnitDetails> {
        return Rx2AndroidNetworking.get(baseUrl.plus("tenant_unit_details/{unit_id}"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addPathParameter("unit_id", unitId.toString())
            .build()
            .getObjectSingle(ResponseUnitDetails::class.java)
    }

    override fun getDuePayment(token: String, unitId: Int): Single<ResponseDuePayment> {
        return Rx2AndroidNetworking.get(baseUrl.plus("due_payment/{unit_id}"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addPathParameter("unit_id", unitId.toString())
            .build()
            .getObjectSingle(ResponseDuePayment::class.java)
    }

    override fun getPaymentHistory(token: String): Single<ResponsePaymentHistory> {
        return Rx2AndroidNetworking.get(baseUrl.plus("paymentDetails"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getObjectSingle(ResponsePaymentHistory::class.java)
    }

    override fun addCreditCardPayment(
        token: String,
        type: String,
        date: String,
        unitId: String,
        amount: String,
        creditCardNo: String,
        expiryDate: String,
        securityCode: String
    ): Single<ResponseGeneralMessage> {
        TODO("Not yet implemented")
    }

    override fun addCashPayment(
        token: String,
        type: String,
        date: String,
        unitId: String,
        amount: String,
        paidTo: String,
        image: File
    ): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.upload(baseUrl.plus("payment"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addMultipartParameter("type", type)
            .addMultipartParameter("date", date)
            .addMultipartParameter("unit_id", unitId)
            .addMultipartParameter("amount", amount)
            .addMultipartParameter("bank", amount)
            .addMultipartParameter("paid_to", paidTo)
            .addMultipartFile("Image", image)
            .build()
            .getObjectSingle(ResponseGeneralMessage::class.java)
    }

    override fun addBankPayment(
        token: String,
        type: String,
        date: String,
        unitId: String,
        amount: String,
        bank: String,
        image: File
    ): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.upload(baseUrl.plus("payment"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addMultipartParameter("type", type)
            .addMultipartParameter("date", date)
            .addMultipartParameter("unit_id", unitId)
            .addMultipartParameter("amount", amount)
            .addMultipartParameter("bank", amount)
            .addMultipartFile("Image", image)
            .build()
            .getObjectSingle(ResponseGeneralMessage::class.java)
    }

    override fun addChequePayment(
        token: String,
        type: String,
        date: String,
        unitId: String,
        amount: String,
        bank: String,
        noOfCheque: String,
        image: File
    ): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.upload(baseUrl.plus("payment"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addMultipartParameter("type", type)
            .addMultipartParameter("date", date)
            .addMultipartParameter("unit_id", unitId)
            .addMultipartParameter("amount", amount)
            .addMultipartParameter("bank", amount)
            .addMultipartParameter("number_of_cheque", noOfCheque)
            .addMultipartFile("Image", image)
            .build()
            .getObjectSingle(ResponseGeneralMessage::class.java)
    }
}