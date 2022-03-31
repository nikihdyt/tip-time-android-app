package com.example.android.tiptime

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.android.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    // mendeklarasikan variabel level teratas di class untuk objek binding
    // keyword "lateinit" digunakan sebagai jaminan bahwa kode Anda akan melakukan inisialisasi variabel sebelum menggunakannya
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inisialisasi objek binding yang akan digunakan untuk mengakses Views di tata letak activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        // menetapkan tampilan konten pada aktivitas ini, dengan menentukan root hierarki tampilan aplikasi ini
        setContentView(binding.root)

        // binding telah diterapkan.
        // sekarang, referensi terhadap View tidak harus menggunakan findViewById(), namun Objek binding secara otomatis menentukan referensi untuk setiap View dalam aplikasi Anda yang memiliki ID

        binding.calculateButton.setOnClickListener { calculateTip() }

        // Set up key listener ke EditText agar menyembunyakan keyboard saat tombol "enter" ditekan
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(
                view,
                keyCode
            )
        }
    }

    private fun calculateTip() {
        // ".text" mengambil text input dari costOfService,
        // ".toString" digunakan utk mengkonversi text yg bertipe Editable menjadi bertipe String
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        // ubah String menjadi Double jika bisa, atau Null jika terjadi error
        val cost = stringInTextField.toDoubleOrNull()
        // jika costOfService tidak diisi double, maka keluar dari fungsi calculateTip()
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            // Instruksi "return" berarti keluar dari metode tersebut tanpa mengeksekusi instruksi lainnya
            return
        }

        // mendapatkan persentase tip dari input Radio yg dipilih user
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        // menghitung tip dan membulatkannya
        var tip = tipPercentage * cost
        if (binding.roundUpSwitch.isChecked) {
            tip = ceil(tip)
        }

        displayTip(tip)
    }

    // memmformat tip sesuai mata uang
    fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    // Key listener untuk menyembunyikan keyboard saat tombol "Enter" ditekan
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}
