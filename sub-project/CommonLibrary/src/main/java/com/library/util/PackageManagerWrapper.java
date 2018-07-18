package com.library.util;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ChangedPackages;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.VersionedPackage;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class PackageManagerWrapper extends PackageManager {
	@Override
	public boolean canRequestPackageInstalls() {
		return false;
	}

	@NonNull
	@Override
	public List<SharedLibraryInfo> getSharedLibraries(int flags) {
		return null;
	}

	@Override
	public PackageInfo getPackageInfo(VersionedPackage versionedPackage, int flags) throws NameNotFoundException {
		return null;
	}

	@Override
	public boolean isInstantApp() {
		return false;
	}

	@Override
	public boolean isInstantApp(String packageName) {
		return false;
	}

	@Override
	public int getInstantAppCookieMaxBytes() {
		return 0;
	}

	@NonNull
	@Override
	public byte[] getInstantAppCookie() {
		return new byte[0];
	}

	@Override
	public void clearInstantAppCookie() {

	}

	@Override
	public void updateInstantAppCookie(@Nullable byte[] cookie) {

	}

	@Nullable
	@Override
	public ChangedPackages getChangedPackages(int sequenceNumber) {
		return null;
	}

	@Override
	public void setApplicationCategoryHint(@NonNull String packageName, int categoryHint) {

	}

	private static Context mContext;
	private PackageManager mPackageManager;
	private static boolean mInited = false;
	private static List<PackageInfo> packageInfoListCache = null;
	private static List<ApplicationInfo> applicationInfoListCache = null;
	private static boolean mIsNeedPackageInfoUpdate = false;
	private static boolean applicationChanged = false;
	private static final boolean DEBUG = false;
	private static final String TAG = "PackageManagerWrapper";
	private static PackageBroadcastReceiver mReceiver;
	private static class PackageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(DEBUG){
				Log.v(TAG, "package changed!");
			}
			mIsNeedPackageInfoUpdate = true;
			applicationChanged = true;
		}
	}

	public static void setNeedPackageInfoUpdate(boolean isNeedPackageInfoUpdate) {
		mIsNeedPackageInfoUpdate = isNeedPackageInfoUpdate;
	}

	public static synchronized void init(Context context){
		mContext = context;
		if(!mInited){
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
			intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
			intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
			intentFilter.addDataScheme("package");
			mReceiver = new PackageBroadcastReceiver();
			mContext.registerReceiver(mReceiver, intentFilter);
			mIsNeedPackageInfoUpdate = true;
			applicationChanged = true;
		}
	}

	public static void uninit(){
		if(mInited){
			mContext.unregisterReceiver(mReceiver);
		}
	}

	public PackageManagerWrapper() {
		//此处发生过一次空指针异常，mContext是null
		//是因为某些代码跑在了init前面，否则init注册广播的时候就会崩溃了
		//因为init是在MoSecurityApplication异步执行的，所以，有可能会比较晚
		//解决方法是使用第二个构造方法，自己提供一个pm吧
		this.mPackageManager = mContext.getPackageManager();
	}

	public PackageManagerWrapper(PackageManager packageManager){
		if(this.mPackageManager == null) {
			this.mPackageManager = packageManager;
		}
	}

	public boolean equals(Object o) {
		return mPackageManager.equals(o);
	}

	public int hashCode() {
		return mPackageManager.hashCode();
	}

	public String toString() {
		return mPackageManager.toString();
	}

	public PackageInfo getPackageInfo(String packageName, int flags) throws NameNotFoundException {
		synchronized (PackageManagerWrapper.class) {
			return mPackageManager.getPackageInfo(packageName, flags);
		}
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public String[] currentToCanonicalPackageNames(String[] names) {
		return mPackageManager.currentToCanonicalPackageNames(names);
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public String[] canonicalToCurrentPackageNames(String[] names) {
		return mPackageManager.canonicalToCurrentPackageNames(names);
	}

	public PermissionInfo getPermissionInfo(String name, int flags) throws NameNotFoundException {
		return mPackageManager.getPermissionInfo(name, flags);
	}

	public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) throws NameNotFoundException {
		return mPackageManager.queryPermissionsByGroup(group, flags);
	}

	public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws NameNotFoundException {
		return mPackageManager.getPermissionGroupInfo(name, flags);
	}

	public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
		return mPackageManager.getAllPermissionGroups(flags);
	}

	public ActivityInfo getActivityInfo(ComponentName component, int flags) throws NameNotFoundException {
		return mPackageManager.getActivityInfo(component, flags);
	}

	@Override
	public ActivityInfo getReceiverInfo(ComponentName component, int flags) throws NameNotFoundException {
		return mPackageManager.getReceiverInfo(component, flags);
	}

	@Override
	public ServiceInfo getServiceInfo(ComponentName component, int flags) throws NameNotFoundException {
		return mPackageManager.getServiceInfo(component, flags);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public ProviderInfo getProviderInfo(ComponentName component, int flags) throws NameNotFoundException {
		return mPackageManager.getProviderInfo(component, flags);
	}

	public ProviderInfo[] getProviderInfo(String packagename) {
		PackageInfo pi;
		ProviderInfo[] providers = null;
		try {
			pi = mPackageManager.getPackageInfo(packagename, PackageManager.GET_PROVIDERS);
			providers = pi.providers;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return providers;
	}

	public int checkPermission(String permName, String pkgName) {
		return mPackageManager.checkPermission(permName, pkgName);
	}

	public boolean addPermission(PermissionInfo info) {
		return mPackageManager.addPermission(info);
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public boolean addPermissionAsync(PermissionInfo info) {
		return mPackageManager.addPermissionAsync(info);
	}

	public void removePermission(String name) {
		mPackageManager.removePermission(name);
	}

	public int checkSignatures(String pkg1, String pkg2) {
		return mPackageManager.checkSignatures(pkg1, pkg2);
	}

	public int checkSignatures(int uid1, int uid2) {
		return mPackageManager.checkSignatures(uid1, uid2);
	}

	public String[] getPackagesForUid(int uid) {
		return mPackageManager.getPackagesForUid(uid);
	}

	public String[] getSystemSharedLibraryNames() {
		return mPackageManager.getSystemSharedLibraryNames();
	}

	public FeatureInfo[] getSystemAvailableFeatures() {
		return mPackageManager.getSystemAvailableFeatures();
	}

	public boolean hasSystemFeature(String name) {
		return mPackageManager.hasSystemFeature(name);
	}

	@Override
	public boolean hasSystemFeature(String name, int version) {
		return false;
	}

	public ResolveInfo resolveActivity(Intent intent, int flags) {
		return mPackageManager.resolveActivity(intent, flags);
	}

	public List<ResolveInfo> queryIntentActivities(Intent intent, int flags) {
		try {
			return mPackageManager.queryIntentActivities(intent, flags);
		} catch (Throwable e) {
			return new ArrayList<ResolveInfo>();
		}
	}

	public List<ResolveInfo> queryIntentActivityOptions(ComponentName caller, Intent[] specifics, Intent intent, int flags) {
		try {
			return mPackageManager.queryIntentActivityOptions(caller, specifics, intent, flags);
		} catch (Throwable e) {
			return new ArrayList<ResolveInfo>();
		}
	}

	public List<ResolveInfo> queryBroadcastReceivers(Intent intent, int flags) {
		try {
			return mPackageManager.queryBroadcastReceivers(intent, flags);
		} catch (Throwable e) {
			return new ArrayList<ResolveInfo>();
		}
	}

	public ResolveInfo resolveService(Intent intent, int flags) {
		return mPackageManager.resolveService(intent, flags);
	}

	public List<ResolveInfo> queryIntentServices(Intent intent, int flags) {
		try {
			return mPackageManager.queryIntentServices(intent, flags);
		} catch (Throwable e) {
			return new ArrayList<ResolveInfo>();
		}
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	public List<ResolveInfo> queryIntentContentProviders(Intent intent, int flags) {
		return mPackageManager.queryIntentContentProviders(intent, flags);
	}

	public ProviderInfo resolveContentProvider(String name, int flags) {
		return mPackageManager.resolveContentProvider(name, flags);
	}

	public List<ProviderInfo> queryContentProviders(String processName, int uid, int flags) {
		try {
			return mPackageManager.queryContentProviders(processName, uid, flags);
		} catch (Throwable e) {
			return new ArrayList<ProviderInfo>();
		}
	}

	public List<InstrumentationInfo> queryInstrumentation(String targetPackage, int flags) {
		return mPackageManager.queryInstrumentation(targetPackage, flags);
	}

	public Drawable getActivityIcon(ComponentName activityName) throws NameNotFoundException {
		return mPackageManager.getActivityIcon(activityName);
	}

	public Drawable getActivityIcon(Intent intent) throws NameNotFoundException {
		return mPackageManager.getActivityIcon(intent);
	}

	@Override
	public Drawable getActivityBanner(ComponentName componentName) throws NameNotFoundException {
		return null;
	}

	@Override
	public Drawable getActivityBanner(Intent intent) throws NameNotFoundException {
		return null;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public Drawable getActivityLogo(ComponentName activityName) throws NameNotFoundException {
		return mPackageManager.getActivityLogo(activityName);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public Drawable getActivityLogo(Intent intent) throws NameNotFoundException {
		return mPackageManager.getActivityLogo(intent);
	}

	public CharSequence getText(String packageName, int resid, ApplicationInfo appInfo) {
		return mPackageManager.getText(packageName, resid, appInfo);
	}

	public XmlResourceParser getXml(String packageName, int resid, ApplicationInfo appInfo) {
		return mPackageManager.getXml(packageName, resid, appInfo);
	}

	public Resources getResourcesForActivity(ComponentName activityName) throws NameNotFoundException {
		return mPackageManager.getResourcesForActivity(activityName);
	}

	public Resources getResourcesForApplication(ApplicationInfo app) throws NameNotFoundException {
		return mPackageManager.getResourcesForApplication(app);
	}

	public Resources getResourcesForApplication(String appPackageName) throws NameNotFoundException {
		return mPackageManager.getResourcesForApplication(appPackageName);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void verifyPendingInstall(int id, int verificationCode) {
		mPackageManager.verifyPendingInstall(id, verificationCode);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setInstallerPackageName(String targetPackage, String installerPackageName) {
		mPackageManager.setInstallerPackageName(targetPackage, installerPackageName);
	}

	public void addPackageToPreferred(String packageName) {
		mPackageManager.addPackageToPreferred(packageName);
	}

	public void removePackageFromPreferred(String packageName) {
		mPackageManager.removePackageFromPreferred(packageName);
	}

	public List<PackageInfo> getPreferredPackages(int flags) {
		try {
			return mPackageManager.getPreferredPackages(flags);
		} catch (Throwable e) {
			return new ArrayList<PackageInfo>();
		}
	}

	public void addPreferredActivity(IntentFilter filter, int match, ComponentName[] set, ComponentName activity) {
		mPackageManager.addPreferredActivity(filter, match, set, activity);
	}

	public void clearPackagePreferredActivities(String packageName) {
		mPackageManager.clearPackagePreferredActivities(packageName);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) {
		mPackageManager.extendVerificationTimeout(id, verificationCodeAtTimeout, millisecondsToDelay);
	}

	public Intent getLaunchIntentForPackage(String packageName) {
		return mPackageManager.getLaunchIntentForPackage(packageName);
	}

	@Override
	public Intent getLeanbackLaunchIntentForPackage(String s) {
		return null;
	}

	public int[] getPackageGids(String packageName) throws NameNotFoundException {
		return mPackageManager.getPackageGids(packageName);
	}

	@Override
	public int[] getPackageGids(String packageName, int flags) throws NameNotFoundException {
		return new int[0];
	}

	@Override
	public int getPackageUid(String packageName, int flags) throws NameNotFoundException {
		return 0;
	}

	public ApplicationInfo getApplicationInfo(String packageName, int flags) throws NameNotFoundException {
		return mPackageManager.getApplicationInfo(packageName, flags);
	}

	/**
	 * 不要用这个方法，用 getInstalledPackages(int flags, int invokeId)，
	 * @deprecated
	 * @param flags
	 * @return
     */
	public List<PackageInfo> getInstalledPackages(int flags){
		throw new RuntimeException("Please use getInstalledPackages(int flags, int invokeId) instead!");
	}

	private Queue<Integer> mLast100InvokeId = new LinkedList<Integer>();

	/**
	 * 传入一个唯一的调用id, 以便监控各个调用的频次，防止高频调用引起pm die问题，
	 * 虽然做了缓存，如无必要尽量不要调用，可能返回空集合，要做好逻辑应对
	 * 如可能，比如获取应用安装的数量，pckagenmae等，尽量考虑使用 getInstalledApplication，Transaction 会比较小，产生pm die 的概率也较小
	 * @param flags
	 * @param invokeId
     * @return
     */
	public List<PackageInfo> getInstalledPackages(int flags, int invokeId) {
		// 记录最后100次调用，用于监测调用频繁现象
		mLast100InvokeId.offer(invokeId);
		if(mLast100InvokeId.size() > 100){
			mLast100InvokeId.poll();
		}
		if(DEBUG){
			Log.v(TAG, "mLast100InvokeId = " + mLast100InvokeId.toString());
		}
		try {
			// 同步一下，防止多线程引起的packagemanager die问题
			synchronized (PackageManagerWrapper.class){
				if(flags == 0 || flags == PackageManager.GET_UNINSTALLED_PACKAGES) {
					if (packageInfoListCache == null || packageInfoListCache.size() == 0 || mIsNeedPackageInfoUpdate) {
						if(DEBUG){
							Log.v(TAG, "cache not valid, refetch it!");
						}
						packageInfoListCache = new ArrayList<>();
						packageInfoListCache.addAll(mPackageManager.getInstalledPackages(0));
						mIsNeedPackageInfoUpdate = false;
					}
					//copy一下，防止外部直接操作缓存
					return new ArrayList<>(packageInfoListCache);
				} else {
					// 其他的flag不可能去一一缓存
					return mPackageManager.getInstalledPackages(flags);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			// 百分之一概率上报崩溃以统计此api调用的频率，小渠道发版开启，发大版本前重新考量此概率或关闭
//			if(System.currentTimeMillis()%10 == 0){
//				IllegalStateException exception = new IllegalStateException("invoke pmw too frequent, last 100 invoke ids are: " + mLast100InvokeId.toString());
//				exception.initCause(e);
//				throw exception;
//			}
		}
		// 返回旧的缓存也比空表要好
		if(packageInfoListCache != null && packageInfoListCache.size() > 0){
			return new ArrayList<>(packageInfoListCache);
		}
		// 好吧，老子没招儿了
		return new ArrayList<>();
	}

	public List<PackageInfo> getInstalledUserPackages(int invokeId){
		List<PackageInfo> origin = getInstalledPackages(0, invokeId);
		List<PackageInfo> result = new ArrayList<>();
		for (PackageInfo info : origin){
			if(info != null && info.applicationInfo != null && ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && (info.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)){
				result.add(info);
			}
		}
		return result;
	}

	public List<String> getInstalledUserPackageNames(int invokeId){
		List<PackageInfo> origin = getInstalledPackages(0, invokeId);
		List<String> result = new ArrayList<>();
		for (PackageInfo info : origin){
			if(info != null && info.applicationInfo != null && ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 || (info.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)){
				result.add(info.packageName);
			}
		}
		return result;
	}

	public List<PackageInfo> getInstalledSystemPackages(int invokeId){
		List<PackageInfo> origin = getInstalledPackages(0, invokeId);
		List<PackageInfo> result = new ArrayList<>();
		for (PackageInfo info : origin){
			if(info != null && info.applicationInfo != null && ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 || (info.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)){
				result.add(info);
			}
		}
		return result;
	}

	public int getInstalledPackageCount(int invokeId){
		List<String> stringList = getInstalledPackagesName(invokeId);
		if (stringList != null) {
			stringList.size();
		}
		return 0;
	}

	public List<String> getInstalledPackagesName(int invokeId){
		List<ApplicationInfo> infos = getInstalledApplications(0, invokeId);
		List<String> result = new ArrayList<>();
		for (ApplicationInfo info : infos){
			result.add(info.packageName);
		}
		//在某些机型上，比如华为，连读取packageinfo也需要权限，我们通过如下方法进行规避
		//如果这个应用连mainactivity都没有，那么可能不会被查询出来，这个只是权宜之计
		if(result.size() == 0){
			//使用set去下重，使用这个方法获取到的package name有大量都是重复的
			HashSet<String> set = new HashSet<>();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			List<ResolveInfo> resolveInfos = queryIntentActivities(intent, 0);
			for (ResolveInfo info : resolveInfos){
				if(info != null && info.activityInfo != null && !TextUtils.isEmpty(info.activityInfo.packageName)){
					set.add(info.activityInfo.packageName);
				}
			}
			result.addAll(set);
		}
		return result;
	}

	public List<String> getInstalledUserPackagesName(int invokeId){
		List<PackageInfo> infos = getInstalledUserPackages(invokeId);
		List<String> result = new ArrayList<>();
		for (PackageInfo info : infos){
			result.add(info.packageName);
		}
		return result;
	}

	public List<String> getInstalledSystemPackagesName(int invokeId){
		List<PackageInfo> infos = getInstalledSystemPackages(invokeId);
		List<String> result = new ArrayList<>();
		for (PackageInfo info : infos){
			result.add(info.packageName);
		}
		return result;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	public List<PackageInfo> getPackagesHoldingPermissions(String[] permissions, int flags) {
		return mPackageManager.getPackagesHoldingPermissions(permissions, flags);
	}

	public String getNameForUid(int uid) {
		return mPackageManager.getNameForUid(uid);
	}

	public List<ApplicationInfo> getInstalledApplications(int flags) {
		throw new RuntimeException("Please use getInstalledApplications(int flags, int invokeId) instead!");
	}

	public List<ApplicationInfo> getInstalledApplications(int flags, int invokeId) {
		try {
			// 同步一下，防止多线程引起的packagemanager die问题
			synchronized (PackageManagerWrapper.class){
				if(flags == 0 || flags == PackageManager.GET_UNINSTALLED_PACKAGES) {
					if (applicationInfoListCache == null || applicationInfoListCache.size() == 0 || applicationChanged) {
						if(DEBUG){
							Log.v(TAG, "cache is not available, refetch!");
						}
						applicationInfoListCache = new ArrayList();
						List<PackageInfo> infos = getInstalledPackages(flags, invokeId);
						// 和packageinfo 共用一份缓存，减少内存占用
						if(infos != null && infos.size() > 0){
							for(PackageInfo pi : infos){
								applicationInfoListCache.add(pi.applicationInfo);
							}
						} else {
							// getInstalledPackages已经不行了，或许 getInstalledApplications 还可以？
							applicationInfoListCache.addAll(mPackageManager.getInstalledApplications(0));
						}
						applicationChanged = false;
					} else {
						// 虽然使用了缓存但是还要记录一下调用记录
						mLast100InvokeId.offer(invokeId);
						if(DEBUG){
							Log.v(TAG, "cache is available, use it!");
						}
					}
					//copy一下，防止外部直接操作缓存
					return new ArrayList(applicationInfoListCache);
				} else {
					return mPackageManager.getInstalledApplications(flags);
				}
			}
		} catch (Throwable e) {
			// 百分之一概率上报崩溃以统计此api调用的频率，小渠道发版开启，发大版本前重新考量此概率或关闭
//			if(System.currentTimeMillis()%10 == 0){
//				IllegalStateException exception = new IllegalStateException("invoke pmw too frequent, last 100 invoke ids are: " + mLast100InvokeId.toString());
//				exception.initCause(e);
//				throw exception;
//			}
		}
		if(applicationInfoListCache != null && applicationInfoListCache.size() > 0){
			return new ArrayList(applicationInfoListCache);
		}
		return new ArrayList();
	}

	public InstrumentationInfo getInstrumentationInfo(ComponentName className, int flags) throws NameNotFoundException {
		return mPackageManager.getInstrumentationInfo(className, flags);
	}

	public Drawable getDrawable(String packageName, int resid, ApplicationInfo appInfo) {
		return mPackageManager.getDrawable(packageName, resid, appInfo);
	}

	public Drawable getDefaultActivityIcon() {
		return mPackageManager.getDefaultActivityIcon();
	}

	public Drawable getApplicationIcon(ApplicationInfo info) {
		return mPackageManager.getApplicationIcon(info);
	}

	public Drawable getApplicationIcon(String packageName) throws NameNotFoundException {
		return mPackageManager.getApplicationIcon(packageName);
	}

	@Override
	public Drawable getApplicationBanner(ApplicationInfo applicationInfo) {
		return null;
	}

	@Override
	public Drawable getApplicationBanner(String s) throws NameNotFoundException {
		return null;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public Drawable getApplicationLogo(ApplicationInfo info) {
		return mPackageManager.getApplicationLogo(info);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public Drawable getApplicationLogo(String packageName) throws NameNotFoundException {
		return mPackageManager.getApplicationLogo(packageName);
	}

	@Override
	public Drawable getUserBadgedIcon(Drawable drawable, UserHandle userHandle) {
		return null;
	}

	@Override
	public Drawable getUserBadgedDrawableForDensity(Drawable drawable, UserHandle userHandle, Rect rect, int i) {
		return null;
	}

	@Override
	public CharSequence getUserBadgedLabel(CharSequence charSequence, UserHandle userHandle) {
		return null;
	}

	public CharSequence getApplicationLabel(ApplicationInfo info) {
		return mPackageManager.getApplicationLabel(info);
	}

	public PackageInfo getPackageArchiveInfo(String archiveFilePath, int flags) {
		return mPackageManager.getPackageArchiveInfo(archiveFilePath, flags);
	}

	public String getInstallerPackageName(String packageName) {
		return mPackageManager.getInstallerPackageName(packageName);
	}

	public int getPreferredActivities(List<IntentFilter> outFilters, List<ComponentName> outActivities, String packageName) {
		return mPackageManager.getPreferredActivities(outFilters, outActivities, packageName);
	}

	public void setComponentEnabledSetting(ComponentName componentName, int newState, int flags) {
		mPackageManager.setComponentEnabledSetting(componentName, newState, flags);
	}

	public int getComponentEnabledSetting(ComponentName componentName) {
		return mPackageManager.getComponentEnabledSetting(componentName);
	}

	public void setApplicationEnabledSetting(String packageName, int newState, int flags) {
		mPackageManager.setApplicationEnabledSetting(packageName, newState, flags);
	}

	public int getApplicationEnabledSetting(String packageName) {
		return mPackageManager.getApplicationEnabledSetting(packageName);
	}

	public boolean isSafeMode() {
		return mPackageManager.isSafeMode();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public PackageInstaller getPackageInstaller() {
		return mPackageManager.getPackageInstaller();
	}

	public boolean isSystemApp(String packageName){
		try {
			ApplicationInfo info = getApplicationInfo(packageName, 0);
			if(info != null){
				if((info.flags & ApplicationInfo.FLAG_SYSTEM) != 0 || (info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
					return true;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public int getAppVersionCode(String packageName){
		try{
			return mPackageManager.getPackageInfo(packageName, 0).versionCode;
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	public Map<String, PackageInfo> getUserPkgInfoMap(int invokeId) {
		List<PackageInfo> list = getInstalledUserPackages(invokeId);
		if (list == null || list.isEmpty()) {
			return null;
		}

		Map<String, PackageInfo> map = new HashMap<String, PackageInfo>(list.size());
		for (PackageInfo info : list) {
			if (info == null) {
				continue ;
			}

			String pkgName = info.packageName;
			if (TextUtils.isEmpty(pkgName)) {
				continue ;
			}

			map.put(pkgName, info);
		}

		return map;
	}

	@Override
	public boolean isPermissionRevokedByPolicy(String permName, String pkgName) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return mPackageManager.isPermissionRevokedByPolicy(permName, pkgName);
		}
		return false;
	}
}
