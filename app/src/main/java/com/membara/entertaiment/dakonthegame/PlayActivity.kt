package com.membara.entertaiment.dakonthegame

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.membara.entertaiment.dakonthegame.databinding.ActivityPlayBinding
import com.membara.entertaiment.dakonthegame.databinding.DialogWinningBinding

class PlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding
    private lateinit var adapterPlayer1: ContainerAdapter
    private lateinit var adapterPlayer2: ContainerAdapter
    private var playerContainers1: MutableList<Int> = mutableListOf()
    private var playerContainers2: MutableList<Int> = mutableListOf()
    private var aiContainers1: MutableList<Int> = mutableListOf()
    private var aiContainers2: MutableList<Int> = mutableListOf()
    private var heuristicAI: MutableList<Int> = mutableListOf()
    private var playerGoal: MutableList<Int> = mutableListOf()
    private var aiGoal: MutableList<Int> = mutableListOf()
    var timer = -200
    var playerTurn = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init players
        initPlayerContainers1()
        initPlayerContainers2()
        initPlayerGoal()
        checkTurn()

        // init adapter
        adapterPlayer1 = ContainerAdapter(playerContainers1, 1, this, this)
        adapterPlayer2 = ContainerAdapter(playerContainers2, 2, this, this)

        // init recyclerview
        binding.rvPlayer1.adapter = adapterPlayer1
        binding.rvPlayer1.layoutManager = object : LinearLayoutManager(
            this,
            HORIZONTAL,
            true
        ) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                // force width of viewHolder here, this will override layout_height from xml
                lp.width = width / 9
                return true
            }
        }

        binding.rvPlayer2.adapter = adapterPlayer2
        binding.rvPlayer2.layoutManager = object : LinearLayoutManager(
            this,
            HORIZONTAL,
            false
        ) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                // force width of viewHolder here, this will override layout_height from xml
                lp.width = width / 9
                return true
            }
        }
    }

    private fun initPlayerContainers1() {
        for (i in 0 until 7) {
            playerContainers1.add(7)
        }

        Log.d("PlayActivity", "initContainer: $playerContainers1")
    }

    private fun initPlayerContainers2() {
        for (i in 0 until 7) {
            playerContainers2.add(7)
        }

        Log.d("PlayActivity", "initContainer: $playerContainers2")
    }

    private fun initPlayerGoal() {
        for (i in 0 until 2) {
            playerGoal.add(0)
        }

        Log.d("PlayActivity", "initGoal: $playerGoal")
    }

    fun moveMarble(marblesRemaining: Int, position: Int, player: Int, status: String) {
        timer = -200
        timer += 200
        var remainingMarbles = marblesRemaining
        var lastPosition =
            if (status == "start") position + 1
            else position
        var newPlayer = player
        Log.d("PlayActivity", "initialRemainingMarbles: $remainingMarbles")
        Log.d("PlayActivity", "initialPlayer: $newPlayer")
        Log.d("PlayActivity", "initialPosition: $position")

        // down to 0 the current position
        if (status == "start" || status == "continueNewMarble") {
            if (player == 1) {
                playerContainers1[position] = 0
                adapterPlayer1.notifyItemChanged(position)
                Log.d("PlayActivity", "player $player: $position refreshed")
            } else {
                playerContainers2[position] = 0
                adapterPlayer2.notifyItemChanged(position)
                Log.d("PlayActivity", "player $player: $position refreshed")
            }
        }
        lastPosition = if (status == "continueNewMarble") lastPosition + 1 else lastPosition

        // marbles moving
        var isBreak = false
        for (i in lastPosition until 8) {
            timer += 200
            Handler().postDelayed(Runnable {
                Log.d("PlayActivity", "remainingMarblesMiddle: $remainingMarbles")
                Log.d("PlayActivity", "positionInsideLoop: $i")
                if (remainingMarbles > 0) {
                    if (player == 1 && i != 7) {
                        playerContainers1[i]++
                        remainingMarbles--
                        binding.rvPlayer1.adapter = adapterPlayer1
                    } else if (player == 2 && i != 7) {
                        playerContainers2[i]++
                        remainingMarbles--
                        binding.rvPlayer2.adapter = adapterPlayer2
                    } else {
                        if (!isBreak) {
                            lastPosition = i
                            if (player == playerTurn) {
                                playerGoal[playerTurn - 1]++
                                remainingMarbles--
                                if (playerTurn == 1) binding.tvGoalsPlayer1.text =
                                    playerGoal[playerTurn - 1].toString()
                                else binding.tvGoalsPlayer2.text =
                                    playerGoal[playerTurn - 1].toString()
                            }

                            newPlayer = changePlayerContainer(player)
                            isBreak = true
                        } else return@Runnable
                    }
                } else {
                    if (!isBreak) {
                        lastPosition = i - 1
                        isBreak = true
                    } else return@Runnable
                }
            }, ((timer).toLong()))
        }

        Handler().postDelayed({
            Log.d("PlayActivity", "player1: $playerContainers1")
            Log.d("PlayActivity", "player2: $playerContainers2")
            Log.d("PlayActivity", "playerGoals: $playerGoal")
            Log.d("PlayActivity", "lastPosition: $lastPosition")
            Log.d("PlayActivity", "remainingMarbles: $remainingMarbles")

            // check if lastPosition is not the goal
            if (lastPosition != 7 && remainingMarbles > 0) {
                // recursion to change player array
                moveMarble(
                    remainingMarbles,
                    lastPosition,
                    newPlayer,
                    "continueNewMarble"
                )
            } else if (lastPosition != 7) {
                // set new remaining marbles
                remainingMarbles = if (newPlayer == 1) {
                    playerContainers1[lastPosition]
                } else {
                    playerContainers2[lastPosition]
                }

                if (remainingMarbles > 1) {
                    // recursion to change player array
                    moveMarble(
                        remainingMarbles,
                        lastPosition,
                        newPlayer,
                        "continueNewMarble"
                    )
                } else {
                    if (!isEnd()) {
                        if (playerTurn == 2) {
                            // BOM
                            if (playerTurn == player) {
                                playerGoal[1] = playerContainers1[6 - lastPosition]
                                playerContainers1[6 - lastPosition] = 0
                                binding.rvPlayer1.adapter = adapterPlayer1
                                binding.tvGoalsPlayer2.text = playerGoal[1].toString()
                            }
                            changeTurn()
                        } else {
                            // BOM
                            if (playerTurn == player) {
                                playerGoal[0] = playerContainers2[6 - lastPosition]
                                playerContainers2[6 - lastPosition] = 0
                                binding.rvPlayer2.adapter = adapterPlayer2
                                binding.tvGoalsPlayer1.text = playerGoal[0].toString()
                            }
                            changeTurn()
                            setHeuristic()
                        }
                    } else endScenario()
                }
            } else {
                // recursion to continue with opposite array
                if (remainingMarbles > 0) {
                    moveMarble(
                        remainingMarbles,
                        0,
                        newPlayer,
                        "continue"
                    )
                    Log.d("PlayActivity", "lastPositionBeforeContinue: $lastPosition")
                    Log.d("PlayActivity", "remainingMarblesBeforeContinue: $remainingMarbles")
                } else if (player != playerTurn) {
                    if (!isEnd()) {
                        changeTurn()
                        if (playerTurn == 2) setHeuristic()
                    } else endScenario()
                } else {
                    if (!isEnd()) {
                        if (playerTurn == 2) setHeuristic()
                    } else endScenario()
                }
            }

            if (isEnd()) endScenario()
        }, timer.toLong())
    }

    private fun isEnd(): Boolean {
        // end container
        val endContainer = mutableListOf<Int>()
        endContainer.add(0, 0)
        endContainer.add(1, 0)

        // check player's container
        for (i in 0 until 7) {
            if (playerContainers1[i] != 0) {
                endContainer[0] = 1
                break
            }
        }

        // check ai's container
        for (i in 0 until 7) {
            if (playerContainers2[i] != 0) {
                endContainer[1] = 1
                break
            }
        }

        // check is it end and return the result?
        return if (endContainer[0] == 0 || endContainer[1] == 0) true
        else playerGoal[0] > 49 || playerGoal[1] > 49
    }

    private fun endScenario() {
        // set winner
        val winner = if (playerGoal[0] > playerGoal[1]) 1 else 2

        // set dialog view
        showDialogWinning(winner)
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogWinning(winner: Int) {
        // initiate
        val dialog = Dialog(this)
        val binding = DialogWinningBinding.inflate(layoutInflater)

        // set dialog property
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false) //TODO
        dialog.setContentView(binding.root)

        // set view
        binding.btnReplay.setOnClickListener {
            dialog.dismiss()
            intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        }
        if (winner == 1) {
            // set text
            binding.tvPlayerScore.text = "${playerGoal[0]} Win"
            binding.tvPlayerScore.setTextColor(Color.parseColor("#7DA639"))

            // set color
            binding.tvAiScore.text = "${playerGoal[1]} Lose"
            binding.tvAiScore.setTextColor(Color.parseColor("#FF0000"))
        } else {
            // set text
            binding.tvPlayerScore.text = "${playerGoal[0]} Lose"
            binding.tvPlayerScore.setTextColor(Color.parseColor("#FF0000"))

            // set color
            binding.tvAiScore.text = "${playerGoal[1]} Win"
            binding.tvAiScore.setTextColor(Color.parseColor("#7DA639"))
        }

        dialog.show()
    }

    private fun changePlayerContainer(player: Int): Int {
        return if (player == 1) 2
        else 1
    }

    private fun changeTurn() {
        playerTurn = if (playerTurn == 1) 2 else 1
        checkTurn()
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun checkTurn() {
        if (playerTurn == 1) {
            binding.tvPlayerTurn.text = "Player's Turn"
            binding.tvPlayerTurn.setTextColor(Color.parseColor("#7DA639"))
        } else {
            binding.tvPlayerTurn.text = "AI's Turn"
            binding.tvPlayerTurn.setTextColor(Color.parseColor("#FF0000"))
        }
    }

    private fun checkHeuristic(
        marblesRemaining: Int,
        position: Int,
        player: Int,
        status: String
    ): Int {
        var remainingMarbles = marblesRemaining
        var lastPosition =
            if (status == "start") position + 1
            else position
        var newPlayer = player

        // down to 0 the current position
        if (status == "start" || status == "continueNewMarble") {
            if (player == 1) {
                aiContainers1[position] = 0
            } else {
                aiContainers2[position] = 0
            }
        }
        lastPosition = if (status == "continueNewMarble") lastPosition + 1 else lastPosition

        // marbles moving
        for (i in lastPosition until 8) {
            if (remainingMarbles > 0) {
                if (player == 1 && i != 7) {
                    aiContainers1[i]++
                    remainingMarbles--
                } else if (player == 2 && i != 7) {
                    aiContainers2[i]++
                    remainingMarbles--
                } else {
                    lastPosition = i
                    if (player == playerTurn) {
                        aiGoal[playerTurn - 1]++
                        remainingMarbles--
                    }

                    newPlayer = changePlayerContainer(player)
                    break
                }
            } else {
                lastPosition = i - 1
                break
            }
        }

        Log.d("PlayActivity", "player1: $aiContainers1")
        Log.d("PlayActivity", "player2: $aiContainers2")
        Log.d("PlayActivity", "aiGoals: $playerGoal")
        Log.d("PlayActivity", "lastPosition: $lastPosition")
        Log.d("PlayActivity", "remainingMarbles: $remainingMarbles")

        // check if lastPosition is not the goal
        if (lastPosition != 7 && remainingMarbles > 0) {
            // recursion to change player array
            checkHeuristic(
                remainingMarbles,
                lastPosition,
                newPlayer,
                "continueNewMarble"
            )
        } else if (lastPosition != 7) {
            // set new remaining marbles
            remainingMarbles = if (newPlayer == 1) {
                aiContainers1[lastPosition]
            } else {
                aiContainers2[lastPosition]
            }

            if (remainingMarbles > 1) {
                // recursion to change player array
                checkHeuristic(
                    remainingMarbles,
                    lastPosition,
                    newPlayer,
                    "continueNewMarble"
                )
            }
        } else {
            // recursion to continue with opposite array
            if (remainingMarbles > 0) {
                checkHeuristic(
                    remainingMarbles,
                    0,
                    newPlayer,
                    "continue"
                )
                Log.d("PlayActivity", "lastPositionBeforeContinue: $lastPosition")
                Log.d("PlayActivity", "remainingMarblesBeforeContinue: $remainingMarbles")
            }
        }

        return aiGoal[1]
    }

    private fun setHeuristic() {
        heuristicAI.clear()
        for (i in 0 until 7) {
            Log.d("PlayActivity", "setHeuristic playerContainers1: $playerContainers1")
            Log.d("PlayActivity", "setHeuristic playerContainers2: $playerContainers2")
            // clear and restart add
            aiContainers1 = playerContainers1.toMutableList()
            aiContainers2 = playerContainers2.toMutableList()
            aiGoal = playerGoal.toMutableList()
            Log.d("PlayActivity", "setHeuristic aiContainers1: $aiContainers1")
            Log.d("PlayActivity", "setHeuristic aiContainers2: $aiContainers2")
            Log.d("PlayActivity", "setHeuristic aiGoal: $aiGoal")

            // set heuristic
            if (aiContainers2[i] != 0) {
                val result = checkHeuristic(
                    aiContainers2[i],
                    i,
                    2,
                    "start"
                )
                Log.d("PlayActivity", "setHeuristic result: $result")
                heuristicAI.add(i, result)
            } else heuristicAI.add(i, 0)
        }
        Log.d("PlayActivity", "setHeuristic: $heuristicAI")

        // check max
        var max = 0
        var bestPosition = 0
        for (i in 0 until 7) {
            if (max < heuristicAI[i]) {
                max = heuristicAI[i]
                bestPosition = i
            }
        }

        // set move for ai
        moveMarble(
            playerContainers2[bestPosition],
            bestPosition,
            2,
            "start"
        )
    }
}