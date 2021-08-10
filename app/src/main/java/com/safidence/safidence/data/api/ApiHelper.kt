package com.safidence.safidence.data.api

class ApiHelper(private val apiService: ApiService) {

    fun login(nic: String, password: String) = apiService.login(nic, password)
    fun forgetPassword(nic: String) = apiService.forgetPassword(nic)
    fun updatePassword(nic: String, password: String) = apiService.updatePassword(nic, password)
    fun logout(token: String) = apiService.logout(token)
    fun getTenantData(token: String) = apiService.getTenantData(token)
    fun getRequestTypes(token: String) = apiService.getRequestTypes(token)
    fun getTenantUnits(token: String) = apiService.getTenantUnits(token)
    fun getTenantRequestStatus(token: String) = apiService.getTenantRequestStatus(token)
    fun getTenantContractExpiry(token: String, unitId:Int) = apiService.getTenantContractExpiry(token, unitId)
    fun tenantRequest(token:String, requestType: Int, subject:String, desc:String, priority:String,
                      availability:String, phone:String, unitId:Int, media:String) =
        apiService.tenantRequest(token, requestType, subject, desc, priority, availability,
            phone, unitId, media)
}