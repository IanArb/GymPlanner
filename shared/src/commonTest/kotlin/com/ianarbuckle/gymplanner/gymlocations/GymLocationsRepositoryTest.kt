package com.ianarbuckle.gymplanner.gymlocations

import com.ianarbuckle.gymplanner.gymlocations.GymLocationsTestDataProvider.DomainGymLocationLists
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsTestDataProvider.GymLocationDtos
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsTestDataProvider.GymLocationLists
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsTestDataProvider.GymLocationsDomain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class GymLocationsRepositoryTest {

    private lateinit var repository: FakeGymLocationsRepository
    private lateinit var fakeRemoteDataSource: FakeGymLocationsRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeGymLocationsRemoteDataSource()
        repository = FakeGymLocationsRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Get Gym Locations Tests ==========

    @Test
    fun `getGymLocations returns success with all locations`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.allLocations

        // When
        val result = repository.getGymLocations()

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(DomainGymLocationLists.allLocations, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.gymLocationsCalls.size)
    }

    @Test
    fun `getGymLocations calls remote data source`() = runTest {
        // When
        repository.getGymLocations()

        // Then
        assertEquals(1, fakeRemoteDataSource.gymLocationsCalls.size)
    }

    @Test
    fun `getGymLocations with multiple locations returns correct count`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.allLocations

        // When
        val result = repository.getGymLocations()

        // Then
        val locations = result.getOrNull()
        assertNotNull(locations)
        assertEquals(6, locations.size)
    }

    @Test
    fun `getGymLocations with single location returns list with one item`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.singleLocation

        // When
        val result = repository.getGymLocations()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(DomainGymLocationLists.singleLocation, result.getOrNull())
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `getGymLocations with no locations returns empty list`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.emptyList

        // When
        val result = repository.getGymLocations()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(DomainGymLocationLists.emptyList, result.getOrNull())
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getGymLocations with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnGymLocations = true
        fakeRemoteDataSource.gymLocationsException = Exceptions.networkError

        // When
        val result = repository.getGymLocations()

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `getGymLocations with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnGymLocations = true
        fakeRemoteDataSource.gymLocationsException = Exceptions.serverError

        // When
        val result = repository.getGymLocations()

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `getGymLocations with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnGymLocations = true
        fakeRemoteDataSource.gymLocationsException = Exceptions.unauthorized

        // When
        val result = repository.getGymLocations()

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `getGymLocations with not found error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnGymLocations = true
        fakeRemoteDataSource.gymLocationsException = Exceptions.notFound

        // When
        val result = repository.getGymLocations()

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.notFound, result.exceptionOrNull())
    }

    @Test
    fun `getGymLocations maps DTOs to domain models correctly`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.allLocations

        // When
        val result = repository.getGymLocations()

        // Then
        val locations = result.getOrNull()
        assertNotNull(locations)
        assertEquals(6, locations.size)
        assertEquals("Clontarf", locations[0].title)
        assertEquals("Coastal Dublin Location", locations[0].subTitle)
        assertTrue(locations[0].description.contains("Modern gym facility"))
    }

    @Test
    fun `getGymLocations preserves location order`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.allLocations

        // When
        val result = repository.getGymLocations()

        // Then
        val locations = result.getOrNull()
        assertNotNull(locations)
        assertEquals(GymLocationsDomain.clontarf, locations[0])
        assertEquals(GymLocationsDomain.astonQuay, locations[1])
        assertEquals(GymLocationsDomain.leopardstown, locations[2])
        assertEquals(GymLocationsDomain.sandymount, locations[3])
        assertEquals(GymLocationsDomain.dunLaoghaire, locations[4])
        assertEquals(GymLocationsDomain.westmanstown, locations[5])
    }

    @Test
    fun `multiple getGymLocations calls work independently`() = runTest {
        // When
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.allLocations
        val result1 = repository.getGymLocations()

        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.singleLocation
        val result2 = repository.getGymLocations()

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(6, result1.getOrNull()?.size)
        assertEquals(1, result2.getOrNull()?.size)
        assertEquals(2, fakeRemoteDataSource.gymLocationsCalls.size)
    }

    @Test
    fun `getGymLocations response contains all location fields`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.allLocations

        // When
        val result = repository.getGymLocations()

        // Then
        val locations = result.getOrNull()
        assertNotNull(locations)
        locations.forEach { location ->
            assertNotNull(location.title)
            assertNotNull(location.subTitle)
            assertNotNull(location.description)
            assertNotNull(location.imageUrl)
        }
    }

    @Test
    fun `getGymLocations with city locations subset works correctly`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.cityLocations

        // When
        val result = repository.getGymLocations()

        // Then
        val locations = result.getOrNull()
        assertNotNull(locations)
        assertEquals(2, locations.size)
        assertEquals(DomainGymLocationLists.cityLocations, locations)
    }

    // ========== Location-specific Tests ==========

    @Test
    fun `getGymLocations with Clontarf location has correct details`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = listOf(GymLocationDtos.clontarf)

        // When
        val result = repository.getGymLocations()

        // Then
        val location = result.getOrNull()?.first()
        assertNotNull(location)
        assertEquals("Clontarf", location.title)
        assertEquals("Coastal Dublin Location", location.subTitle)
        assertTrue(location.description.contains("seafront"))
    }

    @Test
    fun `getGymLocations with Aston Quay location has correct details`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = listOf(GymLocationDtos.astonQuay)

        // When
        val result = repository.getGymLocations()

        // Then
        val location = result.getOrNull()?.first()
        assertNotNull(location)
        assertEquals("Aston Quay", location.title)
        assertEquals("City Centre Location", location.subTitle)
        assertTrue(location.description.contains("city centre"))
    }

    @Test
    fun `getGymLocations with all six Dublin locations is handled`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.allLocations

        // When
        val result = repository.getGymLocations()

        // Then
        val locations = result.getOrNull()
        assertNotNull(locations)
        val titles = locations.map { it.title }
        assertTrue(titles.contains("Clontarf"))
        assertTrue(titles.contains("Aston Quay"))
        assertTrue(titles.contains("Leopardstown"))
        assertTrue(titles.contains("Sandymount"))
        assertTrue(titles.contains("Dun Laoghaire"))
        assertTrue(titles.contains("Westmanstown"))
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `getGymLocations handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnGymLocations = true
        fakeRemoteDataSource.gymLocationsException = Exceptions.timeout

        // When
        val result = repository.getGymLocations()

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.timeout, result.exceptionOrNull())
    }

    @Test
    fun `successful getGymLocations does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.getGymLocations()
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    // ========== Edge Cases ==========

    @Test
    fun `getGymLocations with different location types is handled`() = runTest {
        // Given - Mix of coastal, city, and suburban locations
        fakeRemoteDataSource.gymLocationsResponse =
            listOf(
                GymLocationDtos.clontarf, // Coastal
                GymLocationDtos.astonQuay, // City Centre
                GymLocationDtos.westmanstown, // Suburban
            )

        // When
        val result = repository.getGymLocations()

        // Then
        val locations = result.getOrNull()
        assertNotNull(locations)
        assertEquals(3, locations.size)
        assertTrue(locations[0].subTitle.contains("Coastal"))
        assertTrue(locations[1].subTitle.contains("City Centre"))
        assertTrue(locations[2].subTitle.contains("West Dublin"))
    }

    @Test
    fun `getGymLocations preserves all description details`() = runTest {
        // Given
        fakeRemoteDataSource.gymLocationsResponse = GymLocationLists.allLocations

        // When
        val result = repository.getGymLocations()

        // Then
        val locations = result.getOrNull()
        assertNotNull(locations)
        assertTrue(locations.all { it.description.isNotEmpty() })
        assertTrue(locations.all { it.description.length > 50 }) // All have detailed descriptions
    }

    @Test
    fun `getGymLocations with seaside locations is handled`() = runTest {
        // Given - Locations near water
        fakeRemoteDataSource.gymLocationsResponse =
            listOf(
                GymLocationDtos.clontarf,
                GymLocationDtos.sandymount,
                GymLocationDtos.dunLaoghaire,
            )

        // When
        val result = repository.getGymLocations()

        // Then
        val locations = result.getOrNull()
        assertNotNull(locations)
        assertEquals(3, locations.size)
        // All these locations have water-related descriptions
        val descriptions = locations.map { it.description.lowercase() }
        assertTrue(
            descriptions.any {
                it.contains("seafront") || it.contains("strand") || it.contains("harbor")
            }
        )
    }
}
