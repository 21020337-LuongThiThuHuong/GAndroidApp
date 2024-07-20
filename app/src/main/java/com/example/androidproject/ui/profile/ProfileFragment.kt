package com.example.androidproject.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproject.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fullName.observe(viewLifecycleOwner) {
            binding.profileName.text = it
        }

        viewModel.position.observe(viewLifecycleOwner) {
            binding.profileDomain.text = it
        }

        val adapter = HistoryAdapter()
        binding.progressRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.progressRecyclerView.adapter = adapter

        viewModel.historyList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // Example JSON data, replace with actual data source
        val jsonString = """
            {
               "success":true,
               "message":"",
               "full_name":"Nguyen Quang Chinh",
               "position":"Top Expert",
               "history":[
                  {
                     "title":"2 năm làm việc",
                     "is_up":true
                  },
                  {
                     "title":"3 năm làm việc",
                     "is_up":true
                  },
                  {
                     "title":"4 năm làm việc",
                     "is_up":true
                  },
                  {
                     "title":"Đề suất lên vị trí Top Expert",
                     "is_up":true
                  },
                  {
                     "title":"Giáng chức xuống làm dân thường",
                     "is_up":false
                  }
               ]
            }
        """
        viewModel.loadProfileData(jsonString)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Hành trình"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Hoạt động"))
    }
}
