package com.safidence.safidence.data.api

import com.rx2androidnetworking.Rx2AndroidNetworking
import com.safidence.safidence.data.model.*
import io.reactivex.Single

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

    override fun updatePassword(token: String, nic: String): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.post(baseUrl.plus("updatePassword"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addBodyParameter("nic", nic)
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

    override fun getTenantRequestStatus(token: String): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.get(baseUrl.plus("tenant_request_status"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getObjectSingle(ResponseGeneralMessage::class.java)
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
        requestType: Int,
        subject: String,
        desc: String,
        priority: String,
        availability: String,
        phone: String,
        unitId: Int,
        media: String
    ): Single<ResponseGeneralMessage> {
        return Rx2AndroidNetworking.post(baseUrl.plus("tenant_request"))
            .addHeaders("Accept", "application/json")
            .addHeaders("Authorization", "Bearer $token")
            .addBodyParameter("type_id", requestType.toString())
            .addBodyParameter("subject", subject)
            .addBodyParameter("desc", desc)
            .addBodyParameter("priority", priority)
            .addBodyParameter("availability", availability)
            .addBodyParameter("phone", phone)
            .addBodyParameter("unitId", unitId.toString())
            .addPathParameter("media", media)
            .build()
            .getObjectSingle(ResponseGeneralMessage::class.java)
    }
}