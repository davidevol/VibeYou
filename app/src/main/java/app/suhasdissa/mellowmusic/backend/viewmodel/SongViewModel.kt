package app.suhasdissa.mellowmusic.backend.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.suhasdissa.mellowmusic.MellowMusicApplication
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.repository.SongRepository
import kotlinx.coroutines.launch

class SongViewModel(private val songRepository: SongRepository) : ViewModel() {
    var songs by mutableStateOf<List<Song>>(listOf())
        private set

    var favSongs by mutableStateOf<List<Song>>(listOf())
        private set

    var recentSongs by mutableStateOf<List<Song>>(listOf())
        private set

    fun getAllSongs() {
        viewModelScope.launch {
            songs = songRepository.getAllSongs()
        }
    }

    fun getFavouriteSongs() {
        viewModelScope.launch {
            favSongs = songRepository.getFavSongs()
        }
    }

    fun getRecentSongs() {
        viewModelScope.launch {
            recentSongs = songRepository.getRecentSongs(6)
        }
    }

    fun removeSong(song: Song) {
        viewModelScope.launch {
            songRepository.removeSong(song)
            if (song.isFavourite) {
                getAllSongs()
                getFavouriteSongs()
            } else {
                getAllSongs()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MellowMusicApplication)
                SongViewModel(
                    application.container.songRepository
                )
            }
        }
    }
}