import com.google.gson.annotations.SerializedName

data class ImageRespons (

	@SerializedName("id") val id : Int,
	@SerializedName("backdrops") val backdrops : List<BackdropsImages>,
	@SerializedName("posters") val posters : List<PostersImages>
)