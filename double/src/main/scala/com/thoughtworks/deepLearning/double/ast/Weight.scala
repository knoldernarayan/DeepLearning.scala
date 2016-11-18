package com.thoughtworks.deepLearning.double.ast

import com.thoughtworks.deepLearning.NeuralNetwork._
import cats._
import cats.implicits._
import com.thoughtworks.deepLearning.{Batch, BatchId, NeuralNetwork}
import com.thoughtworks.deepLearning.NeuralNetwork._
import com.thoughtworks.deepLearning.double.LearningRate
import com.thoughtworks.deepLearning.double.utilities.DoubleMonoidBatch

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
final case class Weight(var rawValue: scala.Double)(implicit learningRate: LearningRate)
    extends NeuralNetwork
    with DoubleMonoidBatch
    with BatchId {
  override type Input = Batch
  override type Output = Batch.Aux[Data, Delta]
  override type Open = Output

  override def open() = this

  override def forward(any: BatchId.Aux[Input]) = this

  override def backward(delta: Delta): Unit = {
    rawValue -= delta.value * learningRate()
  }

  override def value = Eval.now(rawValue)

  override def close(): Unit = {}

}
