package igor.petrov.final_android_lvl1.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.databinding.FragmentSearchSettingsSelectYearBinding
import java.util.Calendar

class SearchSettingsSelectYearFragment : Fragment() {

    private var _binding: FragmentSearchSettingsSelectYearBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchSettingsSelectYearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
        }

        binding.textViewYearFromPad12.text = currentYear.toString()
        binding.textViewYearFromPad11.text = (currentYear - 1).toString()
        binding.textViewYearFromPad10.text = (currentYear - 2).toString()
        binding.textViewYearFromPad9.text = (currentYear - 3).toString()
        binding.textViewYearFromPad8.text = (currentYear - 4).toString()
        binding.textViewYearFromPad7.text = (currentYear - 5).toString()
        binding.textViewYearFromPad6.text = (currentYear - 6).toString()
        binding.textViewYearFromPad5.text = (currentYear - 7).toString()
        binding.textViewYearFromPad4.text = (currentYear - 8).toString()
        binding.textViewYearFromPad3.text = (currentYear - 9).toString()
        binding.textViewYearFromPad2.text = (currentYear - 10).toString()
        binding.textViewYearFromPad1.text = (currentYear - 11).toString()
        binding.textViewYearFromPeriod.text = "${currentYear - 11} - ${currentYear}"

        binding.textViewYearToPad12.text = currentYear.toString()
        binding.textViewYearToPad11.text = (currentYear - 1).toString()
        binding.textViewYearToPad10.text = (currentYear - 2).toString()
        binding.textViewYearToPad9.text = (currentYear - 3).toString()
        binding.textViewYearToPad8.text = (currentYear - 4).toString()
        binding.textViewYearToPad7.text = (currentYear - 5).toString()
        binding.textViewYearToPad6.text = (currentYear - 6).toString()
        binding.textViewYearToPad5.text = (currentYear - 7).toString()
        binding.textViewYearToPad4.text = (currentYear - 8).toString()
        binding.textViewYearToPad3.text = (currentYear - 9).toString()
        binding.textViewYearToPad2.text = (currentYear - 10).toString()
        binding.textViewYearToPad1.text = (currentYear - 11).toString()
        binding.textViewYearToPeriod.text = "${currentYear - 11} - ${currentYear}"

        binding.imageViewYearFromLeft.setOnClickListener {
            changeYearFrom(-12)
        }

        binding.imageViewYearFromRight.setOnClickListener {
            changeYearFrom(12)
        }

        binding.imageViewYearToLeft.setOnClickListener {
            changeYearTo(-12)
        }
        binding.imageViewYearToRight.setOnClickListener {
            changeYearTo(12)
        }

        binding.textViewYearFromPad1.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad1.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad2.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad2.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad3.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad3.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad4.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad4.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad5.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad5.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad6.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad6.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad7.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad7.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad8.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad8.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad9.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad9.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad10.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad10.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad11.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad11.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearFromPad12.setOnClickListener {
            searchViewModel.yearFrom = binding.textViewYearFromPad12.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        ///***************------------------------------------**********************************

        binding.textViewYearToPad1.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad1.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad2.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad2.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad3.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad3.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad4.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad4.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad5.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad5.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad6.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad6.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad7.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad7.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad8.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad8.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad9.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad9.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad10.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad10.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad11.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad11.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.textViewYearToPad12.setOnClickListener {
            searchViewModel.yearTo = binding.textViewYearToPad12.text.toString().toInt()
            checkIfSelectedYearInView()
        }

        binding.btnApply.setOnClickListener {
            findNavController().popBackStack()
        }

        checkIfSelectedYearInView()

    }

    private fun changeYearFrom(delta: Int) {
        binding.textViewYearFromPad12.text = (binding.textViewYearFromPad12.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad11.text = (binding.textViewYearFromPad11.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad10.text = (binding.textViewYearFromPad10.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad9.text = (binding.textViewYearFromPad9.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad8.text = (binding.textViewYearFromPad8.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad7.text = (binding.textViewYearFromPad7.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad6.text = (binding.textViewYearFromPad6.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad5.text = (binding.textViewYearFromPad5.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad4.text = (binding.textViewYearFromPad4.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad3.text = (binding.textViewYearFromPad3.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad2.text = (binding.textViewYearFromPad2.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPad1.text = (binding.textViewYearFromPad1.text.toString().toInt() + delta).toString()
        binding.textViewYearFromPeriod.text = "${binding.textViewYearFromPad1.text} - ${binding.textViewYearFromPad12.text}"
        checkIfSelectedYearInView()
    }

    private fun changeYearTo(delta: Int) {
        binding.textViewYearToPad12.text = (binding.textViewYearToPad12.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad11.text = (binding.textViewYearToPad11.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad10.text = (binding.textViewYearToPad10.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad9.text = (binding.textViewYearToPad9.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad8.text = (binding.textViewYearToPad8.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad7.text = (binding.textViewYearToPad7.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad6.text = (binding.textViewYearToPad6.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad5.text = (binding.textViewYearToPad5.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad4.text = (binding.textViewYearToPad4.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad3.text = (binding.textViewYearToPad3.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad2.text = (binding.textViewYearToPad2.text.toString().toInt() + delta).toString()
        binding.textViewYearToPad1.text = (binding.textViewYearToPad1.text.toString().toInt() + delta).toString()
        binding.textViewYearToPeriod.text = "${binding.textViewYearToPad1.text} - ${binding.textViewYearToPad12.text}"
        checkIfSelectedYearInView()
    }

    private fun checkIfSelectedYearInView() {
        binding.textViewYearToPad1.setTextColor(if (binding.textViewYearToPad1.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad2.setTextColor(if (binding.textViewYearToPad2.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad3.setTextColor(if (binding.textViewYearToPad3.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad4.setTextColor(if (binding.textViewYearToPad4.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad5.setTextColor(if (binding.textViewYearToPad5.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad6.setTextColor(if (binding.textViewYearToPad6.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad7.setTextColor(if (binding.textViewYearToPad7.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad8.setTextColor(if (binding.textViewYearToPad8.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad9.setTextColor(if (binding.textViewYearToPad9.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad10.setTextColor(if (binding.textViewYearToPad10.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad11.setTextColor(if (binding.textViewYearToPad11.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearToPad12.setTextColor(if (binding.textViewYearToPad12.text.toString()
                .toInt() == searchViewModel.yearTo) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))

        binding.textViewYearFromPad1.setTextColor(if (binding.textViewYearFromPad1.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad2.setTextColor(if (binding.textViewYearFromPad2.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad3.setTextColor(if (binding.textViewYearFromPad3.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad4.setTextColor(if (binding.textViewYearFromPad4.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad5.setTextColor(if (binding.textViewYearFromPad5.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad6.setTextColor(if (binding.textViewYearFromPad6.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad7.setTextColor(if (binding.textViewYearFromPad7.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad8.setTextColor(if (binding.textViewYearFromPad8.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad9.setTextColor(if (binding.textViewYearFromPad9.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad10.setTextColor(if (binding.textViewYearFromPad10.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad11.setTextColor(if (binding.textViewYearFromPad11.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))
        binding.textViewYearFromPad12.setTextColor(if (binding.textViewYearFromPad12.text.toString()
                .toInt() == searchViewModel.yearFrom) requireContext().getColor(R.color.main_purple) else requireContext().getColor(R.color.black))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}