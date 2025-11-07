package com.example.coroutineapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            performBackgroundTask()
        }
    }

    // 🔹 تابع اصلی: شروع یک Coroutine برای عملیات زمان‌بر
    private fun performBackgroundTask() {
        // ✅ استفاده از lifecycleScope — لغو خودکار هنگام تخریب Activity
        lifecycleScope.launch {
            try {
                // 🔸 ۱. نمایش وضعیت شروع (در Main Thread)
                textView.text = "در حال پردازش..."

                // 🔸 ۲. اجرای عملیات زمان‌بر در پس‌زمینه (غیر از Main Thread)
                val result = withContext(Dispatchers.IO) {
                    simulateLongRunningTask() // این تابع suspend است
                }

                // 🔸 ۳. بروزرسانی UI با نتیجه (به‌طور خودکار در Main Thread)
                textView.text = result

            } catch (e: CancellationException) {
                // لغو شده (مثلاً کاربر صفحه را ترک کرده)
                textView.text = "عملیات لغو شد."
            } catch (e: Exception) {
                // خطای عمومی
                textView.text = "خطا در انجام عملیات!"
                //Toast.makeText(this, "خطا: ${e.message}", Toast.LENGTH_SHORT)
            }
        }
    }

    // 🔹 تابع suspend برای شبیه‌سازی عملیات زمان‌بر (مثل شبکه یا دیتابیس)
    private suspend fun simulateLongRunningTask(): String {
        // فرض کنیم ۲ ثانیه طول می‌کشد (مثل فراخوانی API)
        delay(2000) // ✅ غیرمسدودکننده — فقط coroutine را تعلیق می‌کند

        // می‌توانید اینجا محاسبه واقعی، دسترسی به دیتابیس یا Retrofit بزنید
        return "✅ عملیات با موفقیت انجام شد!\nزمان: ${System.currentTimeMillis()}"
    }
}