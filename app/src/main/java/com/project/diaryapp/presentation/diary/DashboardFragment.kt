package com.project.diaryapp.presentation.diary

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.project.diaryapp.Const
import com.project.diaryapp.R
import com.project.diaryapp.base.Result
import com.project.diaryapp.databinding.FragmentDashboardBinding
import com.project.diaryapp.presentation.BaseFragment
import com.project.diaryapp.presentation.diary.adapter.DiaryPagingAdapter
import com.project.diaryapp.presentation.diary.adapter.LoadingStateAdapter
import com.project.diaryapp.showSnackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DashboardFragment : BaseFragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding by lazy { _binding!! }

    private val diaryViewModel: DiaryViewModel by activityViewModel()

    private val pagingAdapter: DiaryPagingAdapter by lazy {
        DiaryPagingAdapter(
            onItemClicked = {
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_detailDiaryFragment,
                    Bundle().apply {
                        putParcelable(
                            Const.DIARY_OBJECT, it
                        )
                        putString(DetailDiaryFragment.MODE, DetailDiaryFragment.Mode.EDIT.name)
                    }
                )
            },
            onActionButtonClicked = {
                diaryViewModel.archiveDiary(it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObserver()
        initUI()
        initAction()
        initProcess()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initAction(){
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchDiary(binding.edtSearch.text.toString())
            }
            true
        }

        binding.tilSearch.setEndIconOnClickListener {
            binding.edtSearch.setText("")
            searchDiary()
        }

        binding.btnAddDiary.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_detailDiaryFragment, Bundle().also {
                it.putSerializable(DetailDiaryFragment.MODE, DetailDiaryFragment.Mode.ADD.name)
            })
        }
    }

    private fun initUI(){
        with(binding){
            rvDiaryList.apply {
                adapter = pagingAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter{
                        pagingAdapter.retry()
                    }
                )
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
        diaryViewModel.diaryList.observe(viewLifecycleOwner){
            when(it){
                is Result.Loading -> {
                    showLoadingDialog()
                }
                is Result.Success -> {
                    hideLoadingDialog()
                    pagingAdapter.submitData(lifecycle, it.data)
                }
                is Result.Error -> {
                    hideLoadingDialog()
                    binding.root.showSnackbar(it.errorMessage.ifEmpty { getString(R.string.message_unknown_error) })
                }
            }
        }

        diaryViewModel.archiveDiary.observe(viewLifecycleOwner){
            when(it){
                is Result.Loading -> {
                    showLoadingDialog()
                }
                is Result.Success -> {
                    hideLoadingDialog()
                    diaryViewModel.refreshTrigger()
                    binding.root.showSnackbar(getString(R.string.message_archive_diary_success))
                }
                is Result.Error -> {
                    hideLoadingDialog()
                    binding.root.showSnackbar(it.errorMessage.ifEmpty { getString(R.string.message_unknown_error) })
                }
            }
        }

        lifecycleScope.launch {
            diaryViewModel.isDiaryListNeedRefresh.collect {
                searchDiary(binding.edtSearch.text.toString())
            }
        }
    }

    private fun initProcess(){
        diaryViewModel.getAllDiaryList()
    }

    private fun searchDiary(query: String? = null){
        diaryViewModel.getAllDiaryList(query)
    }
}