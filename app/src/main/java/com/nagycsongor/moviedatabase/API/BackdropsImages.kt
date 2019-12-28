import com.google.gson.annotations.SerializedName

data class BackdropsImages (

	@SerializedName("aspect_ratio") val aspect_ratio : Double,
	@SerializedName("file_path") val file_path : String,
	@SerializedName("height") val height : Int,
	@SerializedName("iso_639_1") val iso_639_1 : String,
	@SerializedName("vote_average") val vote_average : Double,
	@SerializedName("vote_count") val vote_count : Int,
	@SerializedName("width") val width : Int
)