package com.thoughtworks.deepLearning
package coproduct.ast

import com.thoughtworks.deepLearning.DifferentiableFunction.Ast
import com.thoughtworks.deepLearning.Differentiable.Batch

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
final case class Tail[Input0 <: Differentiable, HeadData, HeadDelta, TailData <: shapeless.Coproduct,
TailDelta <: shapeless.Coproduct](
    ccons: Ast[Input0, Batch[shapeless.:+:[HeadData, TailData], shapeless.:+:[HeadDelta, TailDelta]]]
) extends DifferentiableFunction {

  final class Output private[Tail] (
      upstream: Batch[shapeless.:+:[HeadData, TailData], shapeless.:+:[HeadDelta, TailDelta]])
      extends Differentiable {
    override type Data = TailData
    override type Delta = TailDelta
    type Input >: Input0

    val value =
      upstream.value.asInstanceOf[shapeless.Inr[TailData, TailData]].tail

    override def backward(delta: Delta): Unit = {
      upstream.backward(shapeless.Inr(delta))
    }

    override def close(): Unit = {
      upstream.close()
    }
  }

  type Input = Input0

  override def forward(input: Input): Output = {
    new Output(ccons.forward(input))
  }

}
