package com.example.mariejosekhalil_pet_adoption.views


import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mariejosekhalil_pet_adoption.R
import com.example.mariejosekhalil_pet_adoption.databinding.ActivityAddPetBinding
import com.example.mariejosekhalil_pet_adoption.model.Pet
import com.example.mariejosekhalil_pet_adoption.viewmodel.PetViewModel

class AddPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPetBinding
    private lateinit var petViewModel: PetViewModel
    private var imageUrl: String = "" // Variable to store the selected image URL

    companion object {
        const val IMAGE_PICK_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize PetViewModel
        petViewModel = ViewModelProvider(this).get(PetViewModel::class.java)

        // Set up the Submit button listener
        binding.sfBtnSubmit.setOnClickListener {
            handleAddPet()
        }

        // Set up the ImageView click listener to pick an image
        binding.imageView.setOnClickListener {
            pickImage()
        }
    }

    private fun handleAddPet() {
        // Collect data from input fields
        val petName = binding.sfEtName.text.toString().trim()
        val petBreed = binding.radioGroup.checkedRadioButtonId.let { id ->
            if (id != -1) findViewById<RadioButton>(id).text.toString() else null
        }
        val petCity = binding.sfEtCity.text.toString().trim()
        val petDistrict = binding.sfEtDistrict.text.toString().trim()
        val petExplanation = binding.sfEtExplanation.text.toString().trim()

        // Validate inputs
        if (petName.isEmpty() || petBreed.isNullOrEmpty() || petCity.isEmpty() || petDistrict.isEmpty() || petExplanation.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a Pet object to store in the database
        val newPet = Pet(
            name = petName,
            breed = petBreed,
            age = "Unknown", // Optional field, default value
            location = "$petCity, $petDistrict",
            description = petExplanation,
            imageUrl = imageUrl,
            id =0,
            userId = "defaultUserId"// when I merge my proj with tony s proj We will replace user Id

        )

        // Save the pet to the database using PetViewModel
        petViewModel.addPet(newPet)

        // Notify user of successful addition
        Toast.makeText(this, "Pet ${newPet.name} added successfully!", Toast.LENGTH_SHORT).show()

        // Clear the input fields
        clearInputs()
    }

    private fun clearInputs() {
        binding.sfEtName.text.clear()
        binding.sfEtCity.text.clear()
        binding.sfEtDistrict.text.clear()
        binding.sfEtExplanation.text.clear()
        binding.radioGroup.clearCheck()
        binding.imageView.setImageResource(R.color.transparent) // Clear the image
        imageUrl = ""
    }

    // Function to pick an image from the gallery
    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // Handle the result of the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            binding.imageView.setImageURI(imageUri)
            imageUrl = imageUri.toString() // Save the URI for storage
        }
    }
}