package com.ianarbuckle.gymplanner.android.dashboard.fakes

import com.ianarbuckle.gymplanner.profile.ProfileRepository
import com.ianarbuckle.gymplanner.profile.domain.Profile

class FakeProfileRepository : ProfileRepository {

    override suspend fun fetchProfile(userId: String): Result<Profile> {
        return mockProfileSuccess()
    }

    private fun mockProfileSuccess(): Result<Profile> {
        return Result.success(Profile("123", "ianarbuckle", "Ian", "Arbuckle", "ian@mail.com"))
    }
}
