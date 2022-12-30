package ru.kggm.domain.entities

data class UserPreferences(
    val folderColumnCountLandscape: Int,
    val folderColumnCountPortrait: Int,
    val fileColumnCountLandscape: Int,
    val fileColumnCountPortrait: Int,
    val supportedFileExtensions: List<String>
) {
    init {
        require(folderColumnCountLandscape in MIN_FOLDER_COLUMN_COUNT_LANDSCAPE..MAX_FOLDER_COLUMN_COUNT_LANDSCAPE)
        require(folderColumnCountPortrait in MIN_FOLDER_COLUMN_COUNT_PORTRAIT..MAX_FOLDER_COLUMN_COUNT_PORTRAIT)
        require(fileColumnCountLandscape in MIN_FILE_COLUMN_COUNT_LANDSCAPE..MAX_FILE_COLUMN_COUNT_LANDSCAPE)
        require(fileColumnCountPortrait in MIN_FILE_COLUMN_COUNT_PORTRAIT..MAX_FILE_COLUMN_COUNT_LANDSCAPE)
        require(supportedFileExtensions.all { File.REGEX_SUPPORTED_FILE_EXTENSION.matches(it) })
    }

    companion object {
        const val MIN_FOLDER_COLUMN_COUNT_LANDSCAPE = 2
        const val MAX_FOLDER_COLUMN_COUNT_LANDSCAPE = 12
        const val DEFAULT_FOLDER_COLUMN_COUNT_LANDSCAPE = 6

        const val MIN_FOLDER_COLUMN_COUNT_PORTRAIT = 2
        const val MAX_FOLDER_COLUMN_COUNT_PORTRAIT = 6
        const val DEFAULT_FOLDER_COLUMN_COUNT_PORTRAIT = 3

        const val MIN_FILE_COLUMN_COUNT_LANDSCAPE = 2
        const val MAX_FILE_COLUMN_COUNT_LANDSCAPE = 12
        const val DEFAULT_FILE_COLUMN_COUNT_LANDSCAPE = 4

        const val MIN_FILE_COLUMN_COUNT_PORTRAIT = 2
        const val MAX_FILE_COLUMN_COUNT_PORTRAIT = 6
        const val DEFAULT_FILE_COLUMN_COUNT_PORTRAIT = 3

        val DEFAULT_SUPPORTED_FILE_EXTENSIONS = listOf(".png", ".jpg", ".jpeg")

        val default get() = UserPreferences(
            folderColumnCountLandscape = DEFAULT_FOLDER_COLUMN_COUNT_LANDSCAPE,
            folderColumnCountPortrait = DEFAULT_FOLDER_COLUMN_COUNT_PORTRAIT,
            fileColumnCountLandscape = DEFAULT_FILE_COLUMN_COUNT_LANDSCAPE,
            fileColumnCountPortrait = DEFAULT_FILE_COLUMN_COUNT_PORTRAIT,
            supportedFileExtensions = DEFAULT_SUPPORTED_FILE_EXTENSIONS
        )
    }
}
