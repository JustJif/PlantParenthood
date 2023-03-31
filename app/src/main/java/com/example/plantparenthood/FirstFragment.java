package com.example.plantparenthood;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.plantparenthood.databinding.FragmentAustenMainmenuBinding;

public class FirstFragment extends Fragment {

    private FragmentAustenMainmenuBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAustenMainmenuBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonQrcodeGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QrCodeGenerator.class);
                startActivity(intent);
            }
        });

        binding.buttonQrcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_QrScanner);
            }
        });

}
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}