package com.project.diaryapp.presentation.diary

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.project.diaryapp.Const
import com.project.diaryapp.R
import com.project.diaryapp.base.Result
import com.project.diaryapp.databinding.FragmentArchivedBinding
import com.project.diaryapp.presentation.BaseFragment
import com.project.diaryapp.presentation.diary.adapter.ArchivedDiaryAdapter
import com.project.diaryapp.showSnackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ArchivedFragment : BaseFragment() {
    private var _binding: FragmentArchivedBinding? = null
    private val binding by lazy { _binding!! }

    private val diaryViewModel: DiaryViewModel by activityViewModel()

    private val archivedDiaryAdapter: ArchivedDiaryAdapter by lazy {
        ArchivedDiaryAdapter(
            context = requireContext(),
            onItemClicked = {
                 findNavController().navigate(R.id.action_archivedFragment_to_detailDiaryFragment,
                     Bundle().apply {
                         putParcelable(Const.DIARY_OBJECT, it)
                         putString(DetailDiaryFragment.MODE, DetailDiaryFragment.Mode.EDIT.name)
                     }
                 )
            },
            onActionButtonClicked = {
                diaryViewModel.unarchiveDiary(it.id)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchivedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObserver()
        initUI()
        initProcess()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initUI(){
        with(binding){
            rvArchivedDiary.apply {
                adapter = archivedDiaryAdapter
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                if(itemDecorationCount < 1){
                    addItemDecoration(object: RecyclerView.ItemDecoration(){
                        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                                    state: RecyclerView.State) {
                            super.getItemOffsets(outRect, view, parent, state)
                            outRect.set(8, 16, 8, 16)
                        }
                    })
                }
            }
        }
    }

    private fun initObserver(){
        diaryViewModel.archivedDiary.observe(viewLifecycleOwner){
            when(it){
                is Result.Loading -> {
                    showLoadingDialog()
                }
                is Result.Success -> {
                    hideLoadingDialog()
                    archivedDiaryAdapter.setData(it.data)
                }
                is Result.Error -> {
                    hideLoadingDialog()
                    binding.root.showSnackbar(it.errorMessage.ifEmpty { getString(R.string.message_unknown_error) })
                }
            }
        }

        diaryViewModel.unarchiveDiary.observe(viewLifecycleOwner){
            when(it){
                is Result.Loading -> {
                    showLoadingDialog()
                }
                is Result.Success -> {
                    hideLoadingDialog()
                    diaryViewModel.refreshTrigger()
                    binding.root.showSnackbar(getString(R.string.message_unarchive_diary_success))
                }
                is Result.Error -> {
                    hideLoadingDialog()
                    binding.root.showSnackbar(it.errorMessage.ifEmpty { getString(R.string.message_unknown_error) })
                }
            }
        }

        lifecycleScope.launch {
            diaryViewModel.isDiaryListNeedRefresh.collect {
                initProcess()
            }
        }
    }

    private fun initProcess(){
        diaryViewModel.getArchivedDiary()
    }
}