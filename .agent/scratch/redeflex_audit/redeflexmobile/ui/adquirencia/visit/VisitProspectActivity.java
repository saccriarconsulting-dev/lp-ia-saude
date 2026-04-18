package com.axys.redeflexmobile.ui.adquirencia.visit;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import androidx.fragment.app.Fragment;
import android.text.format.Formatter;

import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.models.Colaborador;
import com.axys.redeflexmobile.shared.models.adquirencia.RouteClientProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspect;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectAttachment;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectCancelReason;
import com.axys.redeflexmobile.shared.models.adquirencia.VisitProspectQualityQuestion;
import com.axys.redeflexmobile.shared.util.Alerta;
import com.axys.redeflexmobile.shared.util.GPSTracker;
import com.axys.redeflexmobile.shared.util.IGPSTracker;
import com.axys.redeflexmobile.shared.util.Mensagens;
import com.axys.redeflexmobile.shared.util.Notificacoes;
import com.axys.redeflexmobile.shared.util.RequestCode;
import com.axys.redeflexmobile.shared.util.Util_IO;
import com.axys.redeflexmobile.shared.util.Utilidades;
import com.axys.redeflexmobile.ui.adquirencia.visit.route.VisitProspectRouteFragment;
import com.axys.redeflexmobile.ui.adquirencia.visit.visit.VisitProspectVisitFragment;
import com.axys.redeflexmobile.ui.adquirencia.visit.visit.VisitProspectVisitView;
import com.axys.redeflexmobile.ui.base.BaseActivity;
import com.axys.redeflexmobile.ui.component.ComponentProgressLoading;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import id.zelory.compressor.Compressor;

import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.NEGATIVE_SINGLE_INT;
import static com.axys.redeflexmobile.shared.util.Utilidades.retornaTempoAtendimento;
import static com.axys.redeflexmobile.ui.register.customer.RegisterCustomerActivity.REGISTER_FLOW_FLAG;

/**
 * @author Rogério Massa on 29/11/18.
 */

public class VisitProspectActivity extends BaseActivity implements VisitProspectView {

    public static final int FLAG_VISIT = 10;
    public static final int FLAG_VISIT_CANCEL = 11;
    public static final int FLAG_VISIT_QUALITY = 12;
    public static final String PARAM_ID_ROUTE = "idRoute";
    public static final String PARAM_ID_CUSTOMER = "idCustomer";
    public static final String PARAM_ID_PROSPECT = "idProspect";

    @Inject VisitProspectPresenter presenter;

    @BindView(R.id.visit_prospect_cpl_loading) ComponentProgressLoading cplLoading;

    private File tempFile;
    private CountDownTimer timer;
    private VisitProspectVisitView visitProspectVisitView;

    @Override
    protected int getContentView() {
        return R.layout.activity_visita_prospect;
    }

    @Override
    protected ComponentProgressLoading getComponentProgressLoading() {
        return cplLoading;
    }

    @Override
    protected Integer getFrameLayoutId() {
        return R.id.visit_fl_content;
    }

    @Override
    protected void initialize() {
        this.showBackButtonToolbar();
        Intent intent = getIntent();
        presenter.setRouteId(intent.getIntExtra(PARAM_ID_ROUTE, NEGATIVE_SINGLE_INT));

        int customerId = intent.getIntExtra(PARAM_ID_CUSTOMER, NEGATIVE_SINGLE_INT);
        if (customerId > EMPTY_INT) {
            Notificacoes.cancelarNotificacao(this, customerId);
            presenter.getDataByCustomer(customerId);
            return;
        }

        int prospectId = intent.getIntExtra(PARAM_ID_PROSPECT, NEGATIVE_SINGLE_INT);
        if (prospectId > EMPTY_INT) {
            Notificacoes.cancelarNotificacao(this, prospectId);
            presenter.getDataByProspect(prospectId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) timer.start();
    }

    @Override
    protected void onPause() {
        if (timer != null) timer.cancel();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof VisitProspectActivityListener) {
                ((VisitProspectActivityListener) fragment).onBackPressed();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public void setToolbarTitle(String title) {
        if (Util_IO.isNullOrEmpty(title)) setTitle(getString(R.string.app_name));
        else setTitle(title);
    }

    @Override
    public void takePicture() {
        tempFile = Utilidades.setImagem();
        try {
            Utilidades.loadImagefromCamera(this, tempFile, true);
        } catch (Exception e) {
            Mensagens.mensagemErro(this,
                    "Ocorreu um erro inesperado ao iniciar a visita, tente novamente mais tarde",
                    false);
        }
    }

    @Override
    public void initializeFlow() {
        openRootFragment(VisitProspectRouteFragment.newInstance());
    }

    @Override
    public Colaborador getSalesman() {
        return presenter.getSalesman();
    }

    @Override
    public VisitProspect getVisit() {
        return presenter.getVisitProspect();
    }

    @Override
    public RouteClientProspect getCustomer() {
        return presenter.getVisitProspectCustomer();
    }

    @Override
    public Integer getCustomerId() {
        RouteClientProspect client = presenter.getVisitProspectCustomer();
        if (client == null) return null;
        return client.getId();
    }

    @Override
    public String getObservation() {
        return presenter.getObservation();
    }

    @Override
    public void setObservation(String observation) {
        presenter.setObservation(observation);
    }

    @Override
    public void cancelVisit(VisitProspectCancelReason reason, String observation) {
        presenter.cancelVisit(reason, observation);
    }

    @Override
    public void saveQualityVisit(List<VisitProspectQualityQuestion> answers,
                                 List<VisitProspectQualityQuestion> questions) {
        presenter.saveQualityVisit(answers, questions);
    }

    @Override
    public void finishVisit(Integer flowFlag) {
        String message = getString(R.string.visit_prospect_confirm_visit_text);
        if (flowFlag != null && flowFlag == FLAG_VISIT_CANCEL) {
            message = getString(R.string.visit_prospect_confirm_cancel_text);
        } else if (flowFlag != null && flowFlag == FLAG_VISIT_QUALITY) {
            message = getString(R.string.visit_prospect_confirm_quality_text);
        }
        Alerta alerta = new Alerta(this, getString(R.string.app_name), message);
        alerta.show((dialog, which) -> {
            setResult(FLAG_VISIT);
            finish();
        });
    }

    @Override
    public VisitProspectAttachment getVisitProspectAttachment() {
        return presenter.getVisitProspectAttachment();
    }

    @Override
    public IGPSTracker getGpsTracker() {
        return new GPSTracker(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == RequestCode.CaptureImagem && resultCode == Activity.RESULT_OK) {
                prepareImage(Utilidades.getFilePath(this));
            } else if (requestCode == REGISTER_FLOW_FLAG && resultCode == Activity.RESULT_OK) {
                saveVisit();
            }
        } catch (Exception ex) {
            Mensagens.mensagemErro(this, ex.getMessage(), false);
        }
    }

    private void saveVisit() {
        if (visitProspectVisitView != null) {
            visitProspectVisitView.lockScreen();
        }
        presenter.saveVisit();
    }

    private void initializeTimer() {
        timer = new CountDownTimer(1000000000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (visitProspectVisitView == null) return;
                VisitProspect visitProspect = presenter.getVisitProspect();
                if (visitProspect == null) return;
                visitProspectVisitView.setTimer(retornaTempoAtendimento(visitProspect.getDateStart()));
            }

            public void onFinish() {
                // unused
            }
        };
    }

    private void prepareImage(String path) throws Exception {

        File finalPhoto = new Compressor(this)
                .setDestinationDirectoryPath(path)
                .compressToFile(tempFile);

        if (!Utilidades.saveImageResized(this, finalPhoto)) {
            throw new Exception("Imagem não selecionada");
        }

        VisitProspectAttachment attachment = new VisitProspectAttachment();
        attachment.setImage(finalPhoto.getAbsolutePath());
        attachment.setImageSize(Formatter.formatShortFileSize(this, finalPhoto.length()));

        tempFile = null;
        presenter.initializeVisit(attachment);
        startVisit();
    }

    private void startVisit() {
        VisitProspectVisitFragment fragment = VisitProspectVisitFragment.newInstance();
        this.visitProspectVisitView = fragment;
        initializeTimer();
        openFragment(fragment);
    }

    public interface VisitProspectActivityListener {
        void onBackPressed();
    }
}
