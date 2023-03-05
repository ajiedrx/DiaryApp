package com.project.diaryapp.presentation.diary

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.project.diaryapp.Const
import com.project.diaryapp.R
import com.project.diaryapp.base.Result
import com.project.diaryapp.changeDateFormat
import com.project.diaryapp.databinding.FragmentDetailDiaryBinding
import com.project.diaryapp.presentation.BaseFragment
import com.project.diaryapp.presentation.diary.model.Diary
import com.project.diaryapp.showSnackbar
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DetailDiaryFragment : BaseFragment() {

    private var _binding: FragmentDetailDiaryBinding? = null
    private val binding by lazy { _binding!! }

    private val diaryViewModel: DiaryViewModel by activityViewModel()

    private var diaryData: Diary = Diary()

    private var mode: String = ""

    enum class Mode{
        ADD,
        EDIT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailDiaryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
        initUI()
        initAction()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initUI(){
        arguments?.let {
            mode = it.getString(MODE, "")
            if(mode == Mode.ADD.name){
                binding.tvTimestampCreated.visibility = View.GONE
                binding.tvTimestampUpdated.visibility = View.GONE
            } else {
                binding.btnActionBottom.isEnabled = false
                it.getParcelable<Diary>(Const.DIARY_OBJECT)?.let {
                    diaryData = it
                }
                diaryData.let {
                    with(binding){
                        edtDiaryTitle.setText(it.title)
                        if(it.createdAt != it.updatedAt){
                            tvTimestampUpdated.visibility = View.VISIBLE
                        }
                        val dateColor = ContextCompat.getColor(requireContext(), R.color.black)
                        tvTimestampCreated.text = SpannableStringBuilder().append("Created ").bold { color(dateColor){ append(it.createdAt.changeDateFormat()) }}
                        tvTimestampUpdated.text = SpannableStringBuilder().append("Updated ").bold { color(dateColor){ append(it.updatedAt.changeDateFormat()) }}
                        edtDiaryContent.setText(it.content)
                    }
                }
            }
        }
    }

    private fun initAction(){
        with(binding){
            if(mode == Mode.EDIT.name){
                edtDiaryContent.addTextChangedListener {
                    btnActionBottom.isEnabled = it.toString() != diaryData.content
                }
                edtDiaryTitle.addTextChangedListener {
                    btnActionBottom.isEnabled = it.toString() != diaryData.title
                }
            }

            btnActionBottom.setOnClickListener {
                if(!edtDiaryTitle.text.all { (it.isLetterOrDigit() || it.isWhitespace()) } && edtDiaryTitle.text.toString().isNotEmpty()){
                    binding.root.showSnackbar("Invalid title, only alphanumeric allowed")
                } else {
                    when(mode){
                        Mode.ADD.name -> {
                            diaryViewModel.createDiary(
                                edtDiaryTitle.text.toString(),
                                edtDiaryContent.text.toString()
                            )
                        }
                        Mode.EDIT.name -> {
                            diaryViewModel.updateDiary(
                                diaryData.id,
                                edtDiaryTitle.text.toString(),
                                edtDiaryContent.text.toString()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initObservers(){
        diaryViewModel.updateDiary.observe(viewLifecycleOwner){
            when(it){
                is Result.Loading -> {
                    showLoadingDialog()
                }
                is Result.Success -> {
                    hideLoadingDialog()
                    binding.root.showSnackbar(getString(R.string.message_update_diary_success))
                    diaryViewModel.refreshTrigger()
                }
                is Result.Error -> {
                    hideLoadingDialog()
                    binding.root.showSnackbar(it.errorMessage.ifEmpty { getString(R.string.message_unknown_error) })
                }
            }
        }

        diaryViewModel.createDiary.observe(viewLifecycleOwner){
            when(it){
                is Result.Loading -> {
                    showLoadingDialog()
                }
                is Result.Success -> {
                    hideLoadingDialog()
                    binding.root.showSnackbar(getString(R.string.message_create_diary_success))
                    diaryViewModel.refreshTrigger()
                    findNavController().popBackStack(R.id.dashboardFragment, false)
                }
                is Result.Error -> {
                    hideLoadingDialog()
                    binding.root.showSnackbar(it.errorMessage.ifEmpty { getString(R.string.message_unknown_error) })
                }
            }
        }
    }

    companion object{
        const val MODE = "mode"
    }
}