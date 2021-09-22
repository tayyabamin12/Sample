package com.safidence.safidence.data.api

import com.safidence.safidence.data.model.*
import io.reactivex.Single
import java.io.File

interface ApiService {

    fun login(nic: String, password: String): Single<ResponseLogin>
    fun forgetPassword(nic: String): Single<ResponseGeneralMessage>
    fun updatePassword(
        token: String,
        nic: String,
        oldPass: String,
        newPass: String
    ): Single<ResponseGeneralMessage>

    fun logout(token: String): Single<ResponseGeneralMessage>
    fun getTenantData(token: String): Single<ResponseTenantData>
    fun getRequestTypes(token: String): Single<ResponseRequestTypes>
    fun getTenantUnits(token: String): Single<ResponseTenantUnits>
    fun getTenantRequestStatus(token: String): Single<ResponseRequestStatus>
    fun getTenantContractExpiry(token: String, unitId: Int): Single<ResponseTenantContractExpiry>
    fun tenantRequest(
        token: String, requestId: Int, subject: String, desc: String, priority: String,
        availability: String, phone: String, unitId: Int,
        media: File
    ): Single<ResponseGeneralMessage>

    fun getDocTypes(token: String): Single<ResponseDocTypes>
    fun uploadDoc(
        token: String, docId: Int, num: String, country: String, date: String,
        media: File
    ): Single<ResponseGeneralMessage>

    fun getAllDocs(token: String): Single<ResponseAllDocuments>
    fun getTenantAlerts(token: String): Single<ResponseAlerts>
    fun contractRequest(
        token: String, expiryDate: String, date: String,
        unitId: Int, isRenew: Boolean
    ): Single<ResponseGeneralMessage>

    fun getTenantUnitDetails(token: String, unitId: Int): Single<ResponseUnitDetails>
    fun getDuePayment(token: String, unitId: Int): Single<ResponseDuePayment>
    fun getPaymentHistory(token: String): Single<ResponsePaymentHistory>
    fun addCreditCardPayment(
        token: String,
        type: String,
        date: String,
        unitId: String,
        amount: String,
        creditCardNo: String,
        expiryDate: String,
        securityCode: String
    ): Single<ResponseGeneralMessage>
    fun addCashPayment(
        token: String,
        type: String,
        date: String,
        unitId: String,
        amount: String,
        paidTo: String,
        image: File
    ): Single<ResponseGeneralMessage>
    fun addBankPayment(
        token: String,
        type: String,
        date: String,
        unitId: String,
        amount: String,
        bank: String,
        image: File
    ): Single<ResponseGeneralMessage>
    fun addChequePayment(
        token: String,
        type: String,
        date: String,
        unitId: String,
        amount: String,
        bank: String,
        noOfCheque: String,
        image: File
    ): Single<ResponseGeneralMessage>
    fun updateProfile(
        token: String,
        phone: String,
        email: String,
        emergencyName: String,
        emergencyPhone: String
    ): Single<ResponseGeneralMessage>
}