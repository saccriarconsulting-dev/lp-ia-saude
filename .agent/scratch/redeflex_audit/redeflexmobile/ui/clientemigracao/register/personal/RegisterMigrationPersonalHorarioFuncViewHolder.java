package com.axys.redeflexmobile.ui.clientemigracao.register.personal;

import android.view.View;
import android.widget.TextView;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.migracao.CadastroMigracaoSubHorario;
import com.axys.redeflexmobile.ui.base.holder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class RegisterMigrationPersonalHorarioFuncViewHolder extends BaseViewHolder<CadastroMigracaoSubHorario> {
    @BindView(R.id.linha_HorarioFunc_txtDiaSemana) TextView tvDiaSemana;
    @BindView(R.id.linha_HorarioFunc_txtHorario) TextView tvHorario;

    private RegisterMigrationPersonalHorarioFuncViewHolderListener callback;

    RegisterMigrationPersonalHorarioFuncViewHolder(View itemView,
                                                   RegisterMigrationPersonalHorarioFuncViewHolderListener adapterListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        callback = adapterListener;
    }

    @Override
    public void bind(CadastroMigracaoSubHorario object, int position) {
        super.bind(object, position);
        String diaSemana = retorna_DiaSemanaExtenso(object.getDiaAtendimentoId());
        String horarioAtend = object.getHoraInicio() + " - " + object.getHoraFim();

        if (object.getAberto() == 1)
            horarioAtend = "Fechado";

        tvDiaSemana.setText(diaSemana);
        tvHorario.setText(horarioAtend);
    }

    public interface RegisterMigrationPersonalHorarioFuncViewHolderListener {
        CompositeDisposable getCompositeDisposable();
    }

    private String retorna_DiaSemanaExtenso(int diaSemana) {
        if (diaSemana == 7)
            return "Domingo";
        else if (diaSemana == 1)
            return "Segunda-feira";
        else if (diaSemana == 2)
            return "Terça-feira";
        else if (diaSemana == 3)
            return "Quarta-feira";
        else if (diaSemana == 4)
            return "Quinta-feira";
        else if (diaSemana == 5)
            return "Sexta-feira";
        else if (diaSemana == 6)
            return "Sábado";

        return "";
    }
}
