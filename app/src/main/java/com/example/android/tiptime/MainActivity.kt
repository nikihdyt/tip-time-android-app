package com.example.android.tiptime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.android.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    // mendeklarasikan variabel level teratas di class untuk objek binding
    // keyword "lateinit" digunakan sebagai jaminan bahwa kode Anda akan melakukan inisialisasi variabel sebelum menggunakannya
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inisialisasi objek binding yang akan digunakan untuk mengakses Views di tata letak activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        // menetapkan tampilan konten pada aktivitas ini, dengan menentukan root hierarki tampilan aplikasi ini
        setContentView(binding.root)

        // binding telah diterapkan.
        // sekarang, referensi terhadap View tidak harus menggunakan findViewById(), namun Objek binding secara otomatis menentukan referensi untuk setiap View dalam aplikasi Anda yang memiliki ID

        // contoh :
        // Old way with findViewById()
        //   val myButton: Button = findViewById(R.id.my_button)
        //   myButton.text = "A button"
        // Better way with view binding
        //   val myButton: Button = binding.myButton
        //   myButton.text = "A button"
        // Best way with view binding and no extra variable
        //   binding.myButton.text = "A button"

        // penamaan binding :
        // Nama class binding dibuat dengan mengonversi nama file XML menjadi camel case dan menambahkan kata "Binding" ke bagian akhirnya.
        // Demikian pula, referensi untuk setiap tampilan dibuat dengan menghapus garis bawah dan mengonversi nama tampilan menjadi camel case.
        // Misalnya, activity_main.xml menjadi ActivityMainBinding, dan Anda dapat mengakses @id/text_view sebagai binding.textView.

        binding.calculateButton.setOnClickListener{ calculateTip() }
    }

    private fun calculateTip() {
        // ".text" mengambil text input dari costOfService,
        // ".toString" digunakan utk mengkonversi text yg bertipe Editable menjadi bertipe String
        val stringInTextField = binding.costOfService.text.toString()
        // ubah String menjadi Double agar bisa dioperasikan matematika
        val cost = stringInTextField.toDouble()

        // mendapatkan persentase tip dari input Radio yg dipilih user
        val selectedId = binding.tipOptions.checkedRadioButtonId
        val tipPercentage = when (selectedId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        // menghitung tip dan membulatkannya
        var tip = tipPercentage * cost
        val roundUp = binding.roundUpSwitch.isChecked
        if (roundUp) {
            tip = ceil(tip)
        }

        // memmformat tip sesuai mata uang
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }
}