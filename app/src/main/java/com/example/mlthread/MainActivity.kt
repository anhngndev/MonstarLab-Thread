package com.example.mlthread

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity(), OnTouchListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        setAction()

//        thread = Thread(test)
//        thread.start()
//        Thread.sleep(5000)
//        thread.stop()
//        thread = Thread(test2)
//        thread.start()

//        handler.post(test)
//        handler2.post(test2)
    }

    private lateinit var increaseButton: Button
    private lateinit var decreaseButton: Button
    private lateinit var currentNumberTextView: TextView
    private lateinit var background: ConstraintLayout
    private lateinit var restoreNumber: Runnable
    private lateinit var decreaseRunnable: Runnable
    private lateinit var increaseRunnable: Runnable
    private lateinit var pressDecreaseRunnable: Runnable
    private lateinit var pressIncreaseRunnable: Runnable
    private lateinit var test: Runnable
    private lateinit var test2: Runnable
    private var currentNumber: Int = 0
    private var oldY: Float = 0f

    private var handler = Handler()
    private var handler2 = Handler()
    private var thread = Thread()
    private var thread2 = Thread()

    private fun initView() {
        increaseButton = findViewById(R.id.increase)
        decreaseButton = findViewById(R.id.decrease)
        currentNumberTextView = findViewById(R.id.current_number)
        background = findViewById(R.id.layout)
        currentNumberTextView.text = currentNumber.toString()

        increaseRunnable = Runnable {
            currentNumber--
            currentNumberTextView.text = currentNumber.toString()
        }
        decreaseRunnable = Runnable {
            currentNumber++
            currentNumberTextView.text = currentNumber.toString()
        }
        pressIncreaseRunnable = Runnable {
            if (increaseButton.isPressed) {
                currentNumber++
                currentNumberTextView.text = currentNumber.toString()
                handler.post(pressIncreaseRunnable)
            } else handler.postDelayed(restoreNumber, 2000)
        }
        pressDecreaseRunnable = Runnable {
            if (decreaseButton.isPressed) {
                currentNumber--
                currentNumberTextView.text = currentNumber.toString()
                handler.post(pressDecreaseRunnable)
            }else handler.postDelayed(restoreNumber, 2000)
        }
        restoreNumber = Runnable {
            when {
                currentNumber > 0 -> {
                    currentNumber--
                    currentNumberTextView.text = currentNumber.toString()
                    handler.postDelayed(restoreNumber, 30)

                }
                currentNumber < 0 -> {
                    currentNumber++
                    currentNumberTextView.text = currentNumber.toString()

                    handler.postDelayed(restoreNumber, 30)
                }
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAction() {
        background.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_MOVE -> {
                    handler.removeCallbacks(restoreNumber)
                    when {
                        motionEvent.y < oldY -> {
                            handler.post(decreaseRunnable)
                        }
                        motionEvent.y > oldY -> {
                            handler.post(increaseRunnable)
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    handler.postDelayed(restoreNumber, 2000)
                }
            }
            oldY = motionEvent.y
            true
        }

        increaseButton.setOnClickListener {
            handler.removeCallbacks(restoreNumber)
            increase()
        }

        increaseButton.setOnLongClickListener {
            handler.removeCallbacks(restoreNumber)
            handler.postDelayed(pressIncreaseRunnable, 500)
        }

        decreaseButton.setOnClickListener {
            handler.removeCallbacks(restoreNumber)
            decrease()
        }

        decreaseButton.setOnLongClickListener {
            handler.removeCallbacks(restoreNumber)
            handler.postDelayed(pressDecreaseRunnable, 500)
        }

    }

    private fun increase() {
        currentNumber++
        currentNumberTextView.text = currentNumber.toString()
        handler.postDelayed(restoreNumber, 2000)
    }

    private fun decrease() {
        currentNumber--
        currentNumberTextView.text = currentNumber.toString()
        handler.postDelayed(restoreNumber, 2000)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }
}
