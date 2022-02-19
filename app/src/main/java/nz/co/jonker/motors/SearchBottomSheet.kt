package nz.co.jonker.motors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nz.co.jonker.motors.databinding.BottomSheetSearchBinding

class SearchBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSearchBinding? = null

    private val viewModel: SearchViewModel by viewModels(ownerProducer = { requireActivity() })

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener {
            viewModel.search(
                binding.make.text.toString(),
                binding.model.text.toString(),
                binding.year.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
