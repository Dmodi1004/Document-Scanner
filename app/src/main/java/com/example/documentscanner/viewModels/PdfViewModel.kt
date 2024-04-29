package com.example.documentscanner.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.documentscanner.data.repository.PdfRepository
import com.example.documentscanner.models.PdfEntity
import com.example.documentscanner.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PdfViewModel(application: Application) : ViewModel() {

    var isSplashScreen by mutableStateOf(false)
    var showRenameDialog by mutableStateOf(false)
    var loadingDialog by mutableStateOf(false)

    private val pdfRepository = PdfRepository(application)

    private val _pdfStateFlow = MutableStateFlow<Resource<List<PdfEntity>>>(Resource.Idle)
    val pdfStateFlow: StateFlow<Resource<List<PdfEntity>>>
        get() = _pdfStateFlow

    var currentPdfEntity: PdfEntity? by mutableStateOf(null)

    private val _message: Channel<Resource<String>> = Channel()
    val message = _message.receiveAsFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            isSplashScreen = false
        }

        // If Dialog use
        viewModelScope.launch {
            pdfStateFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        loadingDialog = false
                    }

                    is Resource.Error -> {
                        loadingDialog = false
                    }

                    Resource.Idle -> {}
                    Resource.Loading -> {
                        loadingDialog = true
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _pdfStateFlow.emit(Resource.Loading)
            pdfRepository.getPdfList().catch {
                _pdfStateFlow.emit(Resource.Error(it.message.toString()))
                it.printStackTrace()
            }.collect {
                _pdfStateFlow.emit(Resource.Success(it))
            }
        }

    }

    fun insertPdf(pdfEntity: PdfEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // If circular progress bar use in screen
//                _pdfStateFlow.emit(Resource.Loading)

                // If Dialog use
                loadingDialog = true

                val result = pdfRepository.insertPdf(pdfEntity)
                if (result.toInt() != -1) {
                    _message.send(Resource.Success("Pdf inserted successfully"))
                } else {
                    _message.send(Resource.Error("Something went wrong"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _message.send(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
    }

    fun deletePdf(pdfEntity: PdfEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // If circular progress bar use in screen
//                _pdfStateFlow.emit(Resource.Loading)

                // If Dialog use
                loadingDialog = true

                pdfRepository.deletePdf(pdfEntity)
                _message.send(Resource.Success("Pdf deleted successfully"))
            } catch (e: Exception) {
                e.printStackTrace()
                _message.send(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
    }

    fun updatePdf(pdfEntity: PdfEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // If circular progress bar use in screen
//                _pdfStateFlow.emit(Resource.Loading)

                // If Dialog use
                loadingDialog = true

                pdfRepository.updatePdf(pdfEntity)
                _message.send(Resource.Success("Pdf updated successfully"))
            } catch (e: Exception) {
                e.printStackTrace()
                _message.send(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
    }


}