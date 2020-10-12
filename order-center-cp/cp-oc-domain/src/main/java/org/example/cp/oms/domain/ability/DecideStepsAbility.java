package org.example.cp.oms.domain.ability;

import org.example.cp.oms.domain.CoreDomain;
import org.example.cp.oms.spec.DomainAbilities;
import org.example.cp.oms.spec.model.IOrderModel;
import io.github.dddplus.annotation.DomainAbility;
import io.github.dddplus.ext.IDecideStepsExt;
import io.github.dddplus.runtime.BaseDomainAbility;

import javax.validation.constraints.NotNull;
import java.util.List;

@DomainAbility(domain = CoreDomain.CODE, name = "动态决定领域步骤的能力", tags = DomainAbilities.decideSteps)
public class DecideStepsAbility extends BaseDomainAbility<IOrderModel, IDecideStepsExt> {

    public List<String> decideSteps(@NotNull IOrderModel model, String activityCode) {
        return firstExtension(model).decideSteps(model, activityCode);
    }

    @Override
    public IDecideStepsExt defaultExtension(@NotNull IOrderModel model) {
        // 没有默认的扩展点实现
        return null;
    }
}
