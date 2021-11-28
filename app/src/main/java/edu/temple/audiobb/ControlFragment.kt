package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.ViewModelProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ControlFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ControlFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var nowPlayingTextView : TextView
    lateinit var timeRemainingTextView : TextView
    lateinit var timeRemainingSeekBar : SeekBar
    lateinit var playButton : ImageButton
    lateinit var pauseButton : ImageButton
    lateinit var stopButton : ImageButton

    lateinit var viewModel : PlayInfoViewModel
    lateinit var bookViewModel : SelectedBookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewModel = ViewModelProvider(requireActivity()).get(PlayInfoViewModel::class.java)
        bookViewModel = ViewModelProvider(requireActivity()).get(SelectedBookViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var layout = inflater.inflate(R.layout.fragment_control, container, false)

        nowPlayingTextView = layout.findViewById(R.id.NowPlaying)
        timeRemainingTextView = layout.findViewById(R.id.timeRemaining)
        timeRemainingSeekBar = layout.findViewById(R.id.timeRemainingSeekBar)
        playButton = layout.findViewById(R.id.playButton)
        pauseButton = layout.findViewById(R.id.pauseButton)
        stopButton = layout.findViewById(R.id.stopButton)



        playButton.setOnClickListener {
            var book = bookViewModel.getSelectedBook().value
            if(book != null){
                var newPlayInfo = PlayInfo(book.id, book.duration, 0, true, book.title)
                setInformation(newPlayInfo)
                viewModel.setPlayInfo(newPlayInfo)
            }
        }
        pauseButton.setOnClickListener {
            if(viewModel.getPlayInfo().value != null) {
                viewModel.togglePlay()
            }

        }
        stopButton.setOnClickListener {
            viewModel.setPlayInfo(null)
        }


        //todo add seek bar related listeners
        timeRemainingSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
                //intentionally empty as of right now
            }

            override fun onStartTrackingTouch(p0: SeekBar) {
                //intentionally empty as of right now
            }

            override fun onStopTrackingTouch(p0: SeekBar) {

                var playInfo = viewModel.getPlayInfo().value
                if(playInfo != null){
                    var newPlayInfo = PlayInfo(playInfo.id, playInfo.maxRuntime, p0.progress, playInfo.isPlaying, playInfo.title)
                    setInformation(newPlayInfo)
                    viewModel.setPlayInfo(newPlayInfo)
                }
                //timeRemainingTextView.setText("${toTimeString(p0.progress)} / ${toTimeString(p0.max)}")
            }
        })



        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlayInfo().observe(requireActivity(), { playInfo ->
            setInformation(playInfo)
            })
    }

    fun setInformation(_PlayInfo: PlayInfo?){
        if(_PlayInfo != null){
            timeRemainingSeekBar.setMax(_PlayInfo.maxRuntime)
            timeRemainingSeekBar.setProgress(_PlayInfo.currentTime)
            nowPlayingTextView.setText(_PlayInfo.title)
            timeRemainingTextView.setText("${toTimeString(_PlayInfo.currentTime)} / ${toTimeString(_PlayInfo.maxRuntime)}")
        }

    }

    fun toTimeString(_int : Int): String{
        var result = "${_int / 60}:"

        if(_int % 60 < 10){
            result += "0"
        }

        result += "${_int % 60}"

        return result
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ControlFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ControlFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}