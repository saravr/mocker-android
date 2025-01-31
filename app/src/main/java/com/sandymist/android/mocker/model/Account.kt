package com.scribd.android.mocker.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class Account(
    @SerialName("result")
    val result: Result?,
    @SerialName("status")
    val status: Status?
) {
    @Keep
    @Serializable
    data class Result(
        @SerialName("apple_user_id")
        val appleUserId: String?,
        @SerialName("convertible_plans")
        val convertiblePlans: List<String?>?,
        @SerialName("credit_next_accrual_date")
        val creditNextAccrualDate: Int?,
        @SerialName("crossbrand_banners")
        val crossbrandBanners: CrossbrandBanners?,
        @SerialName("eligible_plans")
        val eligiblePlans: List<String?>?,
        @SerialName("email")
        val email: String?,
        @SerialName("id")
        val id: Int?,
        @SerialName("is_paused")
        val isPaused: Boolean?,
        @SerialName("is_referral_creditable")
        val isReferralCreditable: Boolean?,
        @SerialName("membership_info")
        val membershipInfo: MembershipInfo?,
        @SerialName("plans_eligibility")
        val plansEligibility: String?,
        @SerialName("reading_speed_wpm")
        val readingSpeedWpm: Int?,
        @SerialName("referral_urls")
        val referralUrls: ReferralUrls?,
        @SerialName("resubscription_reason")
        val resubscriptionReason: String?,
        @SerialName("subscription_promo_state")
        val subscriptionPromoState: String?,
        @SerialName("unlock_balance")
        val unlockBalance: String?,
        @SerialName("uuid")
        val uuid: String?
    ) {
        @Keep
        @Serializable
        data class CrossbrandBanners(
            @SerialName("annotations")
            val annotations: Boolean?,
            @SerialName("collections")
            val collections: Boolean?,
            @SerialName("saved_content")
            val savedContent: Boolean?
        )

        @Keep
        @Serializable
        data class MembershipInfo(
            @SerialName("bill_date")
            val billDate: String?,
            @SerialName("bill_method")
            val billMethod: String?,
            @SerialName("credit_cache_bust")
            val creditCacheBust: String?,
            @SerialName("current_plan")
            val currentPlan: CurrentPlan?,
            @SerialName("end_date")
            val endDate: Long?,
            @SerialName("has_pmp_access")
            val hasPmpAccess: Boolean?,
            @SerialName("next_bill_price")
            val nextBillPrice: String?,
            @SerialName("next_plan")
            val nextPlan: String?,
            @SerialName("pays_additional_tax")
            val paysAdditionalTax: Boolean?,
            @SerialName("plan_type")
            val planType: String?,
            @SerialName("resume_date")
            val resumeDate: String?,
            @SerialName("status")
            val status: String?,
            @SerialName("unlocks_used_after_last_accural")
            val unlocksUsedAfterLastAccural: Int?
        ) {
            @Keep
            @Serializable
            data class CurrentPlan(
                @SerialName("checkout_store")
                val checkoutStore: String?,
                @SerialName("description")
                val description: String?,
                @SerialName("item_description")
                val itemDescription: String?,
                @SerialName("item_title")
                val itemTitle: String?,
                @SerialName("limited_validity")
                val limitedValidity: String?,
                @SerialName("message")
                val message: String?,
                @SerialName("mobile_display_metadata")
                val mobileDisplayMetadata: MobileDisplayMetadata?,
                @SerialName("monthly_price")
                val monthlyPrice: MonthlyPrice?,
                @SerialName("plan_type")
                val planType: String?,
                @SerialName("product_handle")
                val productHandle: String?,
                @SerialName("subscription")
                val subscription: Boolean?,
                @SerialName("subscription_duration")
                val subscriptionDuration: String?,
                @SerialName("subscription_free_trial_days")
                val subscriptionFreeTrialDays: Int?,
                @SerialName("summary")
                val summary: String?,
                @SerialName("title")
                val title: String?,
                @SerialName("total_price")
                val totalPrice: TotalPrice?,
                @SerialName("unlocks_renewal_amount")
                val unlocksRenewalAmount: Int?
            ) {
                @Keep
                @Serializable
                data class MobileDisplayMetadata(
                    @SerialName("price_display")
                    val priceDisplay: String?
                )

                @Keep
                @Serializable
                data class MonthlyPrice(
                    @SerialName("currency")
                    val currency: String?,
                    @SerialName("value")
                    val value: String?
                )

                @Keep
                @Serializable
                data class TotalPrice(
                    @SerialName("currency")
                    val currency: String?,
                    @SerialName("value")
                    val value: String?
                )
            }
        }

        @Keep
        @Serializable
        data class ReferralUrls(
            @SerialName("email")
            val email: String?,
            @SerialName("facebook_friend")
            val facebookFriend: String?,
            @SerialName("facebook_status")
            val facebookStatus: String?,
            @SerialName("global")
            val global: String?,
            @SerialName("text")
            val text: String?,
            @SerialName("twitter")
            val twitter: String?
        )
    }

    @Keep
    @Serializable
    data class Status(
        @SerialName("code")
        val code: Int?,
        @SerialName("message")
        val message: String?
    )
}