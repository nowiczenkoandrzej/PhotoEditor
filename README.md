# PhotoEditor

In the **PhotoEditor** app you can take photo using your camera or choose a picture from your gallery and do some simple editing with it. You can apply one of three filters, resize and rotate your picture.  ⚡

![End to end gif](pictures/endtoend.gif)

The app was built using libraries such as:
* **Room Database** 🗃
* **Navigation Component** 🗺
* **Android-Image-Cropper** ✂
* **Hilt** 💉
* **Glide** 🖌
* **DataStore** 🗃

with MVVM architecture.

## Navigation

The app uses the **Navigation Component** and contains a single activity, which serves as navHost for all fragments. 

![Nav graph picture](pictures/navgraph.png)


To navige between fragment, I decided to use **actions** which allows to pass arguments.

I also used **Shared Element Transition** resulting in smoother and nicer-looking animations.

![Shared Element Transition](pictures/sharedElement.gif)


## Storing pictures

Every picture is represented by a data class: **PictureItem** which is used throughout the application except of inserting and reading from database. The class has a reference to original picture because it simplifies editing already posted picture.
Class is **Parcelable** to simplifie passing object between fragments.

```kotlin
@Parcelize
data class PictureItem(
    var id: Int? = null,
    val picture: String? = null,
    val originalPicture: String? = null,
    val title: String = "",
    val lastEdit: LocalDate = LocalDate.now(),
): Parcelable
```
There is also a class dedicated to database:
```kotlin
@Entity
data class PictureEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val picture: String,
    val originalPicture: String,
    val title: String,
    val date: LocalDate

)
```
And there are mappers to keep domain and data parts of application seperated:

```kotlin
fun PictureEntity.toPictureItem(): PictureItem {
    return PictureItem(
        id = id,
        picture = picture,
        originalPicture = originalPicture,
        title = title,
        lastEdit = date
    )
}

fun PictureItem.toPictureEntity(): PictureEntity {
    return PictureEntity(
        id = this.id,
        picture = this.picture!!,
        originalPicture = this.originalPicture!!,
        title = this.title,
        date = this.lastEdit
    )
}
```

At the beginnig of develompent I decided to store all pictures as bitmaps in database. I created special **TypeConverter** for Bitmaps to convert them to ByteArrays because Bitmaps can't be stored in Room Database. It resulted in long loading time in list fragment, so I deciced to store Uri path in database what decreased loading time a lot.


## Functionality

Using **PhotoEditor** App you can: 
* choose picture from your gallery,
```kotlin
private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if(uri == null) return@registerForActivityResult

        val picture = PictureItem(
            picture = uri.toString(),
            originalPicture = uri.toString()
        )

        val action = PicturesListFragmentDirections.actionPicturesListFragmentToEditPictureFragment(picture)
        navController.navigate(action)
    }
```
* take photo
```kotlin
private var mImageUri: Uri? = null
    private val getImageFromCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture(),
    ) { isTaken ->
        if(isTaken) {
            mImageUri.let { uri ->
                val picture = PictureItem(
                    picture = uri.toString(),
                    originalPicture = uri.toString()
                )
                val action = PicturesListFragmentDirections.actionPicturesListFragmentToEditPictureFragment(picture)
                navController.navigate(action)
            }

        }
    }
```
and edit it. 

For cropping and rotating pictures I used [Android-Image-Cropper](https://github.com/ArthurHub/Android-Image-Cropper) 
library and integrated it into fragment responsible for editing insted of using seperate Activity just for cropping and rotating pictures, which was easier approach. It results in better UX because user can resize, rotate and apply filters on single screen.

For applying filters on pictures, I created seperate class that uses **ColorMatrixColorFilter** on Bitmap.

It is handled in ViewModel by method: 

```kotlin
private fun addFilter(filterType: FilterType, context: Context){

        val originalUri = _editedPicture.value.originalPicture ?: return
        val pf = PictureFilter(uri = Uri.parse(originalUri), context)

        when(filterType) {
            is FilterType.BlackAndWhite -> _editedBitmap.value = pf.blackAndWhitePicture()
            is FilterType.Invert -> _editedBitmap.value = pf.invertPicture()
            is FilterType.Sepia -> _editedBitmap.value = pf.sepiaPicture()
            is FilterType.Normal -> {
                val uri = Uri.parse(_editedPicture.value.originalPicture)
                _editedBitmap.value = uri.toBitmap(context)

            }
        }
    }
```

The app also has a **SearchBar** what allows user search saved pictures by name.

![SearchBar](pictures/searchbar.gif)
