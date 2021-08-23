package com.safidence.safidence.data.api

import com.safidence.safidence.data.model.*
import io.reactivex.Single
import java.io.File

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
    fun tenantRequest(token:String, requestId: Int, subject:String, desc:String, priority:String,
                      availability:String, phone:String, unitId:Int,
                      media:File): Single<ResponseGeneralMessage>
    fun getDocTypes(token:String): Single<ResponseDocTypes>
    fun uploadDoc(token:String, docId: Int, num:String, country:String, date:String,
                      media:File): Single<ResponseGeneralMessage>
    fun getAllDocs(token:String): Single<ResponseAllDocuments>
}