package com.axys.redeflexmobile.shared.dao.registerrate;

import com.axys.redeflexmobile.shared.bd.DBTaxaMdr;
import com.axys.redeflexmobile.shared.bd.registerrate.DBRegistrationSimulationFee;
import com.axys.redeflexmobile.shared.models.adquirencia.TaxaMdr;
import com.axys.redeflexmobile.shared.models.registerrate.RegistrationSimulationFee;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * @author Lucas Marciano on 23/04/2020
 */
public class RegistrationSimulationFeeDaoImpl implements RegistrationSimulationFeeDao {

    private final DBRegistrationSimulationFee db;
    private final DBTaxaMdr dbTaxaMdr;

    public RegistrationSimulationFeeDaoImpl(DBRegistrationSimulationFee db, DBTaxaMdr dbTaxaMdr) {
        this.db = db;
        this.dbTaxaMdr = dbTaxaMdr;
    }

    @Override
    public Single<Boolean> add(@NotNull RegistrationSimulationFee tax) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                db.add(tax);

                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<Boolean> addAll(List<RegistrationSimulationFee> listTaxes, int prospectId) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                for (RegistrationSimulationFee tax : listTaxes) {
                    tax.setIdProspecting(prospectId);
                    db.add(tax);
                }

                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<RegistrationSimulationFee>> getByProspectId(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                List<RegistrationSimulationFee> list = db.getAllTaxesByProspectId(id);

                emitter.onSuccess(list);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<RegistrationSimulationFee> getById(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                RegistrationSimulationFee item = db.getById(id);

                emitter.onSuccess(item);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<RegistrationSimulationFee>> getAll() {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                List<RegistrationSimulationFee> list = db.getAll(null, null);

                emitter.onSuccess(list);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<Boolean> deleteById(int id) {
        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    return;
                }

                db.deleteById(id);

                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public Single<List<RegistrationSimulationFee>> loadTaxes(
            final int personType,
            final double estimatedAverageBilling,
            final int idTradingTerm,
            final int idMcc) {

        return Single.create(emitter -> {
            try {
                if (emitter.isDisposed()) return;

                final double safe = Double.isFinite(estimatedAverageBilling) ? estimatedAverageBilling : 0d;
                final double revenue;
                try {
                    revenue = new java.math.BigDecimal(String.valueOf(safe))
                            .setScale(2, java.math.RoundingMode.HALF_UP)
                            .doubleValue();
                } catch (NumberFormatException ex) {
                    timber.log.Timber.w(ex, "[MDR][PARSE] estimatedAverageBilling=%s",
                            String.valueOf(estimatedAverageBilling));
                    if (!emitter.isDisposed()) {
                        emitter.onError(new IllegalArgumentException(
                                "Não foram encontradas taxas para os parâmetros informados"));
                    }
                    return;
                }

                final java.util.List<TaxaMdr> taxList = dbTaxaMdr.getAllTaxFlagTypes(
                        personType, revenue, idTradingTerm, idMcc);

                if (taxList == null || taxList.isEmpty() || taxList.size() < 3) {
                    timber.log.Timber.w("[MDR][EMPTY] personType=%d revenue=%.2f termId=%d mcc=%d",
                            personType, revenue, idTradingTerm, idMcc);
                    if (!emitter.isDisposed()) {
                        emitter.onError(new IllegalArgumentException(
                                "Não foram encontradas taxas para os parâmetros informados"));
                    }
                    return;
                }

                if (!emitter.isDisposed()) {
                    timber.log.Timber.d("[MDR][LOAD] personType=%d revenue=%.2f termId=%d mcc=%d rows=%d",
                            personType, revenue, idTradingTerm, idMcc, taxList.size());
                    emitter.onSuccess(convertTaxMdrToSimulationFee(taxList));
                }

            } catch (Exception e) {
                timber.log.Timber.e(e, "[MDR][ERROR] personType=%d revenue=%.2f termId=%d mcc=%d",
                        personType, estimatedAverageBilling, idTradingTerm, idMcc);
                if (!emitter.isDisposed()) emitter.onError(e);
            }
        });
    }

    private List<RegistrationSimulationFee> convertTaxMdrToSimulationFee(List<TaxaMdr> taxList) {
        List<RegistrationSimulationFee> convertedList = new ArrayList<>();

        for (TaxaMdr tax : taxList) {
            RegistrationSimulationFee item = new RegistrationSimulationFee();
            item.setIdFlag(tax.getFlagType());
            item.setDebitValue(tax.getTaxDebit());
            item.setCreditValue(tax.getTaxCredit());
            item.setCreditSixValue(tax.getTaxUntilSix());
            item.setCreditMoreThanSixValue(tax.getTaxBiggerSix());
            item.setAutomaticAnticipation(tax.getAnticipation());

            convertedList.add(item);
        }

        return convertedList;
    }
}