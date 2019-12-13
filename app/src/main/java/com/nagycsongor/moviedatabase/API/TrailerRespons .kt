import com.google.gson.annotations.SerializedName

data class TrailerRespons (

	@SerializedName("id") val id : Int,
	@SerializedName("results") val results : List<TrailerResults>
)