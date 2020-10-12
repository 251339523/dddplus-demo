package org.example.cp.oms.domain.step.submitorder;

import lombok.extern.slf4j.Slf4j;
import io.github.dddplus.annotation.Step;
import io.github.dddplus.runtime.DDD;
import io.github.dddplus.step.ReviseStepsException;
import org.example.cp.oms.domain.ability.PresortAbility;
import org.example.cp.oms.domain.ability.ReviseStepsAbility;
import org.example.cp.oms.domain.model.OrderModel;
import org.example.cp.oms.domain.step.SubmitOrderStep;
import org.example.cp.oms.spec.Steps;
import org.example.cp.oms.spec.exception.OrderException;

import javax.validation.constraints.NotNull;
import java.util.List;

@Step(value = "submitBasicStep")
@Slf4j
public class BasicStep extends SubmitOrderStep {

    @Override
    public void execute(@NotNull OrderModel model) throws OrderException {
        model.setStep(this.stepCode());
        List<String> revisedSteps = DDD.findAbility(ReviseStepsAbility.class).revisedSteps(model);
        if (revisedSteps != null) {
            log.info("重新编排步骤：{}", revisedSteps);

            // 通过异常，来改变后续步骤
            throw new ReviseStepsException().withSubsequentSteps(revisedSteps);
        }

        log.info("presorting...");
        DDD.findAbility(PresortAbility.class).presort(model);
    }

    @Override
    public void rollback(@NotNull OrderModel model, @NotNull OrderException cause) {
        log.info("will rollback now...");
    }

    @Override
    public String stepCode() {
        return Steps.SubmitOrder.BasicStep;
    }
}
