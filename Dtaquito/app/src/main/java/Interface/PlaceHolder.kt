package Interface

import Beans.GameRoom
import Beans.LoginRequest
import Beans.LoginResponse
import Beans.SportSpace
import Beans.Usuarios
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceHolder {

    @POST("api/v1/users")
    fun createUser(@Body user: Usuarios): Call<Usuarios>

    @POST("api/v1/authentication/sign-in")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("api/v1/sport-spaces/all")
    fun getAllSportSpaces(): Call<List<SportSpace>>

    @GET("api/v1/sport-spaces/{id}")
    fun getSportSpaceById(@Path("id") id: Int): Call<SportSpace>

    @GET("api/v1/rooms/all")
    fun getAllRooms(): Call<List<GameRoom>>

    @FormUrlEncoded
    @POST("api/v1/rooms/create")
    fun createRoom(
        @Field("creatorId") creatorId: Long,
        @Field("sportSpaceId") sportSpaceId: Long,
        @Field("day") day: String,
        @Field("openingDate") openingDate: String,
        @Field("roomName") roomName: String
    ): Call<GameRoom>

    @GET("api/v1/rooms/{id}")
    fun getRoomById(@Path("id") id: Int): Call<GameRoom>

    @GET("api/v1/users/{id}")
    fun getUserId(@Path("id") id: Int): Call<Usuarios>


}