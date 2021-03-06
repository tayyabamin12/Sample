package com.safidence.safidence.data.repository

import com.safidence.safidence.api.ApiHelper
import com.safidence.safidence.data.model.*
import io.reactivex.Single
import java.io.File

class MainRepository(private val apiHelper: ApiHelper) {

    fun login(nic: String, password: String): Single<ResponseLogin> {
        return apiHelper.login(nic, password)
    }

    fun forgetPassword(nic: String): Single<ResponseGeneralMessage> {
        return apiHelper.forgetPassword(nic)
    }

    fun logout(token: String): Single<ResponseGeneralMessage> {
        return apiHelper.logout(token)
    }

    fun updatePassword(token: String, nic: String, oldPass: String, newPass: String):
            Single<ResponseGeneralMessage> {
        return apiHelper.updatePassword(
            token, nic,
            oldPass, newPass
        )
    }

    fun getTenantData(token: String): Single<ResponseTenantData> {
        return apiHelper.getTenantData(token)
    }

    fun getRequestTypes(token: String): Single<ResponseRequestTypes> {
        return apiHelper.getRequestTypes(token)
    }

    fun getTenantUnits(token: String): Single<ResponseTenantUnits> {
        return apiHelper.getTenantUnits(token)
    }

    fun getTenantRequestStatus(token: String): Single<ResponseRequestStatus> {
        return apiHelper.getTenantRequestStatus(token)
    }

    fun getTenantContractExpiry(token: String, unitId: Int): Single<ResponseTenantContractExpiry> {
        return apiHelper.getTenantContractExpiry(token, unitId)
    }

    fun tenantRequest(
        token: String, requestType: Int,
        subject: String,
        desc: String,
        priority: String,
        availability: String,
        phone: String,
        unitId: Int, media: File
    ): Single<ResponseGeneralMessage> {
        return apiHelper.tenantRequest(
            token, requestType, subject, desc,
            priority, availability, phone, unitId, media
        )
    }

    fun getDocTypes(token: String): Single<ResponseDocTypes> {
        return apiHelper.getDocTypes(token)
    }

    fun uploadDoc(
        token: String, docId: Int, num: String, country: String,
        date: String, media: File
    ): Single<ResponseGeneralMessage> {
        return apiHelper.uploadDoc(token, docId, num, country, date, media)
    }

    fun getAllDocs(token: String): Single<ResponseAllDocuments> {
        return apiHelper.getAllDocs(token)
    }

    fun getTenantAlerts(token: String): Single<ResponseAlerts> {
        return apiHelper.getTenantAlerts(token)
    }

    fun contractRequest(
        token: String, expiryDate: String, date: String, unitId: Int,
        isRenew: Boolean
    ): Single<ResponseGeneralMessage> {
        return apiHelper.contractRequest(token, expiryDate, date, unitId, isRenew)
    }

    fun getContractDetails(token: String, unitId: Int): Single<ResponseContract> {
        return apiHelper.getContractDetails(token, unitId)
    }

    fun getTenantUnitDetails(token: String, unitId: Int): Single<ResponseUnitDetails> {
        return apiHelper.getTenantUnitDetails(token, unitId)
    }

    fun getDuePayment(token: String, unitId: Int): Single<ResponseDuePayment> {
        return apiHelper.getDuePayment(token, unitId)
    }

    fun getPaymentHistory(token: String): Single<ResponsePaymentHistory> {
        return apiHelper.getPaymentHistory(token)
    }

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
    ): Single<ResponseGeneralMessage> {
        return apiHelper.addCreditCardPayment(
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
    }

    fun addCashPayment(
        token: String,
        type: String,
        date: String,
        paidTill: String,
        unitId: String,
        amount: String,
        paidTo: String,
        image: File
    ): Single<ResponseGeneralMessage> {
        return apiHelper.addCashPayment(token, type, date, paidTill, unitId, amount, paidTo, image)
    }

    fun addBankPayment(
        token: String,
        type: String,
        date: String,
        paidTill: String,
        unitId: String,
        amount: String,
        bank: String,
        image: File
    ): Single<ResponseGeneralMessage> {
        return apiHelper.addBankPayment(token, type, date, paidTill, unitId, amount, bank, image)
    }

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
    ): Single<ResponseGeneralMessage> {
        return apiHelper.addChequePayment(
            token,
            type,
            date,
            paidTill,
            unitId,
            amount,
            bank,
            noOfCheque,
            image
        )
    }

    fun updateProfile(
        token: String,
        phone: String,
        email: String,
        emergencyName: String,
        emergencyPhone: String
    ): Single<ResponseGeneralMessage> {
        return apiHelper.updateProfile(token, phone, email, emergencyName, emergencyPhone)
    }

    fun getPolicies(
        token: String
    ): Single<ResponsePolicies> {
        return apiHelper.getPolicies(token)
    }
}