package androidx.fragment.app;


import android.nfc.Tag;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by YoKey on 16/1/22.
 */
public class FragmentationMagician {

    private static final String TAG = "FragmentationMagician";

    public static boolean isStateSaved(FragmentManager fragmentManager) {
        if (!(fragmentManager instanceof FragmentManagerImpl))
            return false;
        try {
            FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl) fragmentManager;
            return fragmentManagerImpl.isStateSaved();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Like {@link FragmentManager#popBackStack()}} but allows the commit to be executed after an
     * activity's state is saved.  This is dangerous because the action can
     * be lost if the activity needs to later be restored from its state, so
     * this should only be used for cases where it is okay for the UI state
     * to change unexpectedly on the user.
     */
    public static void popBackStackAllowingStateLoss(final FragmentManager fragmentManager) {
        FragmentationMagician.hookStateSaved(fragmentManager, new Runnable() {
            @Override
            public void run() {
                fragmentManager.popBackStack();
            }
        });
    }

    /**
     * Like {@link FragmentManager#popBackStackImmediate()}} but allows the commit to be executed after an
     * activity's state is saved.
     */
    public static void popBackStackImmediateAllowingStateLoss(final FragmentManager fragmentManager) {
        FragmentationMagician.hookStateSaved(fragmentManager, new Runnable() {
            @Override
            public void run() {
                fragmentManager.popBackStackImmediate();
            }
        });
    }

    /**
     * Like {@link FragmentManager#popBackStackImmediate(String, int)}} but allows the commit to be executed after an
     * activity's state is saved.
     */
    public static void popBackStackAllowingStateLoss(final FragmentManager fragmentManager, final String name, final int flags) {
        FragmentationMagician.hookStateSaved(fragmentManager, new Runnable() {
            @Override
            public void run() {
                fragmentManager.popBackStack(name, flags);
            }
        });
    }

    /**
     * Like {@link FragmentManager#executePendingTransactions()} but allows the commit to be executed after an
     * activity's state is saved.
     */
    public static void executePendingTransactionsAllowingStateLoss(final FragmentManager fragmentManager) {
        FragmentationMagician.hookStateSaved(fragmentManager, new Runnable() {
            @Override
            public void run() {
                fragmentManager.executePendingTransactions();
            }
        });
    }

    public static List<Fragment> getActiveFragments(FragmentManager fragmentManager) {
        return fragmentManager.getFragments();
    }

    private static void hookStateSaved(FragmentManager fragmentManager, Runnable runnable) {
        if (!(fragmentManager instanceof FragmentManagerImpl)) return;
        FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl) fragmentManager;
        if (isStateSaved(fragmentManager)) {
            boolean tempStateSaved = getFieldValueByFieldName("mStateSaved", fragmentManagerImpl);
            boolean tempStopped = getFieldValueByFieldName("mStopped", fragmentManagerImpl);
            setFieldValueByFieldName("mStateSaved", fragmentManagerImpl, false);
            setFieldValueByFieldName("mStopped", fragmentManagerImpl, false);
            runnable.run();
            setFieldValueByFieldName("mStopped", fragmentManagerImpl, tempStopped);
            setFieldValueByFieldName("mStateSaved", fragmentManagerImpl, tempStateSaved);
        } else {
            runnable.run();
        }
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param object
     * @return
     */
    private static boolean getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (boolean) field.get(object);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    /**
     * 根据属性名设置属性值
     *
     * @param fieldName
     * @param object
     * @param value
     */
    private static void setFieldValueByFieldName(String fieldName, Object object, boolean value) {
        try {
            //获取obj类的字节文件对象
            Class cls = object.getClass();
            //获取该类的成员变量
            Field field = cls.getDeclaredField(fieldName);
            //取消语言访问检查
            field.setAccessible(true);
            //给变量赋值
            field.set(object, value);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }
}