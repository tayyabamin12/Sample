package com.safidence.safidence.api

import java.io.File

class ApiHelper(private val apiService: ApiService) {

    fun login(nic: String, password: String) = apiService.login(nic, password)
    fun forgetPassword(nic: String) = apiService.forgetPassword(nic)
    fun updatePassword(token: String, nic: String, oldPass: String, newPass: String) =
        apiService.updatePassword(token, nic, oldPass, newPass)

    fun logout(token: String) = apiService.logout(token)
    fun getTenantData(token: String) = apiService.getTenantData(token)
    fun getRequestTypes(token: String) = apiService.getRequestTypes(token)
    fun getTenantUnits(token: String) = apiService.getTenantUnits(token)
    fun getTenantRequestStatus(token: String) = apiService.getTenantRequestStatus(token)
    fun getTenantContractExpiry(token: String, unitId: Int) =
        apiService.getTenantContractExpiry(token, unitId)

    fun tenantRequest(
        token: String, requestType: Int, subject: String, desc: String, priority: String,
        availability: String, phone: String, unitId: Int, media: File
    ) =
        apiService.tenantRequest(
            token, requestType, subject, desc, priority, availability,
            phone, unitId, media
        )

    fun getDocTypes(token: String) = apiService.getDocTypes(token)
    fun uploadDoc(
        token: String, docId: Int, num: String, country: String, date: String,
        media: File
    ) = apiService.uploadDoc(token, docId, num, country, date, media)

    fun getAllDocs(token: String) = apiService.getAllDocs(token)
    fun getTenantAlerts(token: String) = apiService.getTenantAlerts(token)
    fun contractRequest(
        token: String, expiryDate: String, date: String, unitId: Int,
        isRenew: Boolean
    ) = apiService.contractRequest(
        token, expiryDate, date,
        unitId, isRenew
    )

    fun getTenantUnitDetails(token: String, unitId: Int) =
        apiService.getTenantUnitDetails(token, unitId)

    fun getContractDetails(token: String, unitId: Int) =
        apiService.getContractDetails(token, unitId)

    fun getDuePayment(token: String, unitId: Int) = apiService.getDuePayment(token, unitId)
    fun getPaymentHistory(token: String) = apiService.getPaymentHistory(token)
    fun addCreditCardPayment(
        token: String,
        type: String,
        date: String,
        paidTill: String,
        unitId: String,
        amount: String,
        creditCardNo: String,
        expiryDate: String,
        securityCode: String
    ) = apiService.addCreditCardPayment(
        token,
        type,
        date,
        paidTill,
        unitId,
        amount,
        creditCardNo,
        expiryDate,
        securityCode
    )
    fun addCashPayment(
        token: String,
        type: String,
        date: String,
        paidTill: String,
        unitId: String,
        amount: String,
        paidTo: String,
        image: File
    ) = apiService.addCashPayment(token, type, date, paidTill, unitId, amount, paidTo, image)
    fun addBankPayment(
        token: String,
        type: String,
        date: String,
        paidTill: String,
        unitId: String,
        amount: String,
        bank: String,
        image: File
    ) = apiService.addBankPayment(token, type, date, paidTill, unitId, amount, bank, image)

    fun addChequePayment(
        token: String,
        type: String,
        date: String,
        paidTill: String,
        unitId: String,
        amount: String,
        bank: String,
        noOfCheque: String,
        image: File
    ) = apiService.addChequePayment(token, type, date, paidTill, unitId, amount, bank, noOfCheque, image)
    fun updateProfile(
        token: String,
        phone: String,
        email: String,
        emergencyName: String,
        emergencyPhone: String
    ) =
        apiService.updateProfile(token, phone, email, emergencyName, emergencyPhone)

    fun getPolicies(token: String) = apiService.getPolicies(token)
}