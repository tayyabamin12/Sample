package com.safidence.safidence.data.api

import com.safidence.safidence.data.model.*
import io.reactivex.Single

interface ApiService {

    fun login(nic:String, password:String): Single<ResponseLogin>
    fun forgetPassword(nic:String): Single<ResponseGeneralMessage>
    fun updatePassword(token:String, nic:String, oldPass:String, newPass:String): Single<ResponseGeneralMessage>
    fun logout(token:String): Single<ResponseGeneralMessage>
    fun getTenantData(token:String): Single<ResponseTenantData>
    fun getRequestTypes(token:String): Single<ResponseRequestTypes>
    fun getTenantUnits(token:String): Single<ResponseTenantUnits>
    fun getTenantRequestStatus(token:String): Single<ResponseRequestStatus>
    fun getTenantContractExpiry(token:String, unitId: Int): Single<ResponseTenantContractExpiry>
    fun tenantRequest(token:String, requestType: Int, subject:String, desc:String, priority:String,
                      availability:String, phone:String, unitId:Int,
                      media:String): Single<ResponseGeneralMessage>
}