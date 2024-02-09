package igor.petrov.final_android_lvl1.presentation.ui.photo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.FragmentFullScreenPhotoBinding
import igor.petrov.final_android_lvl1.entity.Photo
import igor.petrov.final_android_lvl1.presentation.adapters.FullScreenPhotoAdapterAdapter
import igor.petrov.final_android_lvl1.presentation.ui.film.FilmFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FragmentFullScreenPhoto : Fragment() {
    private val hideHandler = Handler(Looper.myLooper()!!)
    private val fragmentPhotoViewModel: FragmentPhotoViewModel by activityViewModels()
    private val filmFragmentViewModel: FilmFragmentViewModel by activityViewModels()

    private val fragmentScope = CoroutineScope(Dispatchers.Main)

    @Suppress("InlinedApi")
    private val hidePart2Runnable = Runnable {
       (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        fullscreenContentControls?.visibility = View.VISIBLE
    }
    private var visible: Boolean = false
    private val hideRunnable = Runnable { hide() }

    //private var dummyButton: Button? = null
    private var fullscreenContent: View? = null
    private var fullscreenContentControls: View? = null

    private var _binding: FragmentFullScreenPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullScreenPhotoBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var photoFrom:String? = ""
        var photoUrl:String? = ""
        var photoPosition:Int? = 0



        arguments?.let {
            photoFrom = it.getString("photoFrom")
            photoUrl = it.getString("photoUrl")
            photoPosition = it.getInt("photoPosition")
            fragmentPhotoViewModel.kinopoiskId = it.getInt("kinopoiskId")
        }



        val fullScreenPhotoAdapter = FullScreenPhotoAdapterAdapter{photo: Photo, position:Int -> onPhotoClicked(photo,position)}
        binding.recyclerViewFullScreenPhoto.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        //binding.recyclerViewFullScreenPhoto.layoutManager = GridLayoutManager(requireContext(),1)
        binding.recyclerViewFullScreenPhoto.adapter = fullScreenPhotoAdapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerViewFullScreenPhoto)

        when (photoFrom) {
            "fromFilm" ->{
                fragmentScope.launch {
                    filmFragmentViewModel.galleryList.collect {
                        fullScreenPhotoAdapter.submitData(it)
                        binding.recyclerViewFullScreenPhoto.layoutManager!!.scrollToPosition(photoPosition!!)
                    }
                }
            }
            "fromPhoto" -> {
                fragmentScope.launch {
                    fragmentPhotoViewModel.photoList.collect {
                        fullScreenPhotoAdapter.submitData(it)
                        binding.recyclerViewFullScreenPhoto.layoutManager!!.scrollToPosition(photoPosition!!)
                        binding.recyclerViewFullScreenPhoto.scrollToPosition(photoPosition!!)
                    }
                }
            }
        }

        fullScreenPhotoAdapter.loadStateFlow.onEach {
            if (it.refresh != LoadState.Loading) {
                binding.recyclerViewFullScreenPhoto.scrollToPosition(photoPosition!!)
            }
        }.launchIn(fragmentScope)

        visible = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent?.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //dummyButton?.setOnTouchListener(delayHideTouchListener)



    }

    private fun onPhotoClicked(photo: Photo, position: Int) {
        //dummy function
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Clear the systemUiVisibility flag
        activity?.window?.decorView?.systemUiVisibility = 0
        show()
    }

    override fun onDestroy() {
        super.onDestroy()
        fullscreenContent = null
        fullscreenContentControls = null
        (activity as? AppCompatActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)?.isVisible = true
    }

    private fun toggle() {
        if (visible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        fullscreenContentControls?.visibility = View.GONE
        visible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @Suppress("InlinedApi")
    private fun show() {
        // Show the system bar
        fullscreenContent?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        visible = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        fragmentScope.cancel()
    }
}