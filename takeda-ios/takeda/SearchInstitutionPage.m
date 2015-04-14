//
//  SearchInstitutionPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "SearchInstitutionPage.h"

#import "Spot.h"
#import "NonHierarchicalDistanceBasedAlgorithm.h"
#import "GClusterAlgorithm.h"

#import "GQuadItem.h"
#import "GStaticCluster.h"
#import "GDefaultClusterRenderer.h"
#import "ExProButton.h"

#define kMaximumMyLocationSearchTime 8.0f

@interface SearchInstitutionPage (){
//    GMSMapView *mapView_;
    GMSCameraPosition *camera;
    GClusterManager *clusterManager_;
    GMSPolyline *polyline;
    NSMutableArray *gsmMarkersList;

    NSMutableDictionary *selectedCity;
    NSMutableDictionary *selectedSpec;

    BOOL searchShowed;
    BOOL filterShowed;
    
    BOOL reloading;
    BOOL pullTorefreshVisible;
    BOOL firstLocationUpdate;
    BOOL myLocationUpdating;
    BOOL routeIsShowing;
    
    UIView *markerView;
    CLLocation *myCurrentLocation;
    UIBarButtonItem *searchBtnItem;
    UIBarButtonItem *filterBtnItem;
}

@end

@implementation SearchInstitutionPage
@synthesize mapView_;
@synthesize lpuDetail;
@synthesize searchCity;
@synthesize searchSpec;
@synthesize filtrSpec;
@synthesize searchContainer;
@synthesize locMan;

//@synthesize lpuList;
@synthesize LPUArray;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setupInterface];
//    if ([UserDefaults valueForKey:@"lastCity"]){
//        [self preloadCity:[UserDefaults valueForKey:@"lastCity"]];
//    } else {
    
//        [mapView_ addObserver:self
//                   forKeyPath:@"myLocation"
//                      options:NSKeyValueObservingOptionNew
//                       context:NULL];
    
//    }
    

    [self drawMap];
    [self initData];
//    [self performSelector:@selector(scrollToMyLocation) withObject:nil afterDelay:1.0f];
    
    locMan = [CLLocationManager new];
    locMan.delegate = self;
    locMan.distanceFilter = kCLDistanceFilterNone;
    locMan.desiredAccuracy = kCLLocationAccuracyBest;
    if ([locMan respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
        [locMan requestWhenInUseAuthorization];
    } else {
    if ([CLLocationManager authorizationStatus]!=kCLAuthorizationStatusNotDetermined){
        [self prepareLocationManager];
    }
    }
    
}


-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.container.clipsToBounds = YES;
    searchBtnItem = [self menuBarBtnWithImageName:@"search_icon" selector:@selector(showHideSearch:) forTarget:self];
    filterBtnItem = [self menuBarBtnWithImageName:@"filterIcon.png" selector:@selector(showHideFilter:) forTarget:self];
    self.navigationItem.rightBarButtonItems = @[filterBtnItem, searchBtnItem];
    
    
    if (!self.isAppearFromBack){
        [self selectView:self.listMapSwitch.selectedSegmentIndex];
       // searchShowed = YES;
       // [self showHideSearch:self.showHideSearchBtn];
    }
}

-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
}

-(NSArray*)lpuList{
    return LPUArray.filteredArray;
}

-(void)scrollToMyLocation{
    [mapView_ animateToLocation:mapView_.myLocation.coordinate];
    [mapView_ animateToZoom:14.0f];
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
//    searchShowed = NO;
//    [self showHideSearch:nil];
}


-(void)setupInterface{
    if (_refreshHeaderView == nil) {
        EGORefreshTableHeaderView *view = [[EGORefreshTableHeaderView alloc] initWithFrame:CGRectMake(0.0f, 0.0f - self.tableView.height+43, self.tableView.width, self.tableView.height)];
        view.delegate = self;
        _refreshHeaderView = view;
    }
    [self.tableView addSubview:_refreshHeaderView];
    [_refreshHeaderView refreshLastUpdatedDate];
    
    _drawRouteBtn.layer.cornerRadius = _drawRouteBtn.height/2;
    _drawRouteBtn.layer.borderColor = RGB(200, 200, 200).CGColor;
    _drawRouteBtn.layer.borderWidth = 1.0f;
    _drawRouteBtn.backgroundColor = [UIColor colorWithWhite:1.0f alpha:0.75f];

    _normZoomBtn.layer.cornerRadius = _normZoomBtn.height/2;
    _normZoomBtn.layer.borderColor = RGB(200, 200, 200).CGColor;
    _normZoomBtn.layer.borderWidth = 1.0f;
    _normZoomBtn.backgroundColor = [UIColor colorWithWhite:1.0f alpha:0.75f];
    
    _searchBtn.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:17.0];
    [_searchBtn setTitleColor:RGB(54, 65, 71) forState:UIControlStateNormal];
    _searchBtn.contentEdgeInsets = UIEdgeInsetsMake(-3, 0, 0, 0);
    _searchBtn.layer.borderColor = RGB(54, 65, 71).CGColor;
    _searchBtn.layer.borderWidth = 1;
    _searchBtn.layer.cornerRadius = 5.0f;
    
//    searchContainer.backgroundColor = RGBA(53, 65, 71, 0.4);
    searchContainer.backgroundColor = RGBA(255, 255, 255, 0.7);

//    searchCity.backgroundColor = [UIColor colorWithWhite:0.6 alpha:0.4];
//    searchSpec.backgroundColor = [UIColor colorWithWhite:0.6 alpha:0.4];

    searchCity.backgroundColor = [UIColor colorWithWhite:1 alpha:0.85];
    searchSpec.backgroundColor = [UIColor colorWithWhite:1 alpha:0.85];
    filtrSpec.backgroundColor = [UIColor colorWithWhite:1 alpha:0.85];
    
    self.listMapSwitch.clipsToBounds = YES;
    self.listMapSwitch.layer.cornerRadius = 4.0f;
    self.filtrSpec.layer.cornerRadius = 4.0f;

    searchCity.leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, searchCity.height)];
    searchCity.leftViewMode = UITextFieldViewModeAlways;
    filtrSpec.leftViewMode = UITextFieldViewModeAlways;

    searchSpec.leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, searchSpec.height)];
    searchSpec.leftViewMode = UITextFieldViewModeAlways;
    filtrSpec.leftViewMode = UITextFieldViewModeAlways;

    searchCity.layer.cornerRadius = 5.0f;
    searchSpec.layer.cornerRadius = 5.0f;
    filtrSpec.layer.cornerRadius = 5.0f;

    [searchCity setPlaceholderColor:RGB(53, 65, 71)];
    [searchSpec setPlaceholderColor:RGB(53, 65, 71)];
    [filtrSpec setPlaceholderColor:RGB(53, 65, 71)];

    searchCity.itemsKey = @"name";
    searchSpec.itemsKey = @"name";
    filtrSpec.itemsKey = @"name";

    [searchCity setupChangeBlock:^(NSString *text) {
        [self searchCityChangeBlock:text forSender:searchCity];
    }];
    [searchCity setupFinishBlock:^(id sender){
        [self selectCityBlock:sender];
    }];
    
    [searchSpec setupChangeBlock:^(NSString *text) {
        [self searchSpecChangeBlock:text forSender:searchSpec];
    }];
    [searchSpec setupFinishBlock:^(id sender){
        [self selectSpecBlock:sender];
    }];
    
    [filtrSpec setupChangeBlock:^(NSString *text) {
        [self filtrSpecChangeBlock:text forSender:filtrSpec];
    }];
    
    [filtrSpec setupFinishBlock:^(id sender){
        [self selectFiltrBlock:sender];
    }];

    searchContainer.y = -95;
}


-(void)initData{
    // name
    // adres
    // location
    // category
    // short description
    
    
    
}

-(void)checkForLocationSearch{
    if (myLocationUpdating){
    [self removeActivityIdicator];
    myLocationUpdating = NO;
    [locMan stopUpdatingLocation];
    CLLocation *currentLocation;
        if (locMan.location){
            currentLocation = locMan.location;
        } else if (mapView_.myLocation){
            currentLocation = mapView_.myLocation;
        } else {
            currentLocation = [self defaultLocation];
        }
        [self preloadCityForLocation:currentLocation];
    }
}

#pragma mark - Loction Manager delegate

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations
{
    NSLog(@"loc man delegate success");
    [self removeActivityIdicator];
    myLocationUpdating = NO;
    [locMan stopUpdatingLocation];
    CLLocation *currentLocation;
    if (locations.count>0){
        currentLocation = [locations objectAtIndex:0];
    } else {
        NSLog(@"loc man delegate set default location");
        currentLocation = [self defaultLocation];
    }
    NSLog(@"call preload location from loc Man delegete");

    [self preloadCityForLocation:currentLocation];

}

-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error{
    myLocationUpdating = NO;
    [locMan stopUpdatingLocation];
    NSLog(@"call preload location from loc Man delegeteFailError");
    [self preloadCityForLocation:[self defaultLocation]];
}

-(void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status{
    if ([CLLocationManager authorizationStatus]==kCLAuthorizationStatusAuthorized||[CLLocationManager authorizationStatus]==kCLAuthorizationStatusAuthorizedWhenInUse){
        [self prepareLocationManager];
    }
}

#pragma mark - make routes


-(void)setSelectedLPU:(NSMutableDictionary *)selectedLPU{
    _selectedLPU = selectedLPU;
    [self checkToMakeRoute];
}

-(IBAction)switchMakeRoute:(UIButton*)sender{
    if (myCurrentLocation==nil){
        sender.selected = NO;
        [self removeRoute];
        return;
    }
    sender.selected = !sender.selected;
    [self checkToMakeRoute];

}

-(void)checkToMakeRoute{
    if (_drawRouteBtn.selected){ // если отображение маршрута включено
        if (_selectedLPU){
            [self makeRoute];
        } else {
            [self removeRoute];
        }
    } else {                    // отображение маршрута выключено
        [self removeRoute];
    }
}

-(void)makeRoute{
    NSMutableArray *waypoints = [NSMutableArray new];
    NSMutableArray *waypointStrings = [NSMutableArray new];

    if (_selectedLPU==nil){
        [self removeRoute];
        return;
    }
    
    double lat = [_selectedLPU[@"lat"] doubleValue];
    double lon = [_selectedLPU[@"lng"] doubleValue];
    CLLocationCoordinate2D startPoint = mapView_.myLocation?mapView_.myLocation.coordinate:myCurrentLocation.coordinate;

    CLLocation *lpUloc = [[CLLocation alloc] initWithLatitude:lat longitude:lon];
    
    GMSMarker *startMarker = [GMSMarker markerWithPosition:myCurrentLocation.coordinate];
    [waypoints addObject:startMarker];
    GMSMarker *lpuMarker = [GMSMarker markerWithPosition:lpUloc.coordinate];
    [waypoints addObject:lpuMarker];
    
    
    NSString *startPositionString = [NSString stringWithFormat:@"%f,%f",startPoint.latitude,startPoint.longitude];
    NSString *finishPositionString = [NSString stringWithFormat:@"%f,%f",lpuMarker.position.latitude,lpuMarker.position.longitude];

    [waypointStrings addObject:startPositionString];
    [waypointStrings addObject:finishPositionString];

    if (waypoints.count > 1) {
        NSDictionary *query = @{ @"sensor" : @"false",
                                 @"waypoints" : waypointStrings };
        MDDirectionService *mds = [[MDDirectionService alloc] init];
        SEL selector = @selector(addDirections:);
        [mds setDirectionsQuery:query
                   withSelector:selector
                   withDelegate:self];
    }else{
        NSLog(@"No route created");
    }
   // [self addMapAnnotation];
}

-(void)removeRoute{
    polyline.map = nil;
    polyline = nil;
}

-(void)addDirections:(NSDictionary *)json{
    if ([json[@"status"] isEqualToString:@"ZERO_RESULTS"]||![json[@"routes"] isKindOfClass:[NSArray class]]||[json[@"routes"] count]==0){
        return;
    }
    NSDictionary *routes = json[@"routes"][0];
    NSDictionary *route = routes[@"overview_polyline"];
    NSString *overview_route = route[@"points"];
    GMSPath *path = [GMSPath pathFromEncodedPath:overview_route];
    polyline.map = nil;
    polyline = [GMSPolyline polylineWithPath:path];
    polyline.strokeColor = RGB(154, 30, 241);
    polyline.strokeWidth = 3.0f;
    polyline.map = mapView_;
    [self scrollToRouteSize];
}

-(void)scrollToRouteSize{
    if (_selectedLPU==nil){
        return;
    }

    double lat = [_selectedLPU[@"lat"] doubleValue];
    double lon = [_selectedLPU[@"lng"] doubleValue];

    CLLocation *lpUloc = [[CLLocation alloc] initWithLatitude:lat longitude:lon];
    GMSMarker *startMarker = [GMSMarker markerWithPosition: mapView_.myLocation?mapView_.myLocation.coordinate:myCurrentLocation.coordinate];
    GMSMarker *lpuMarker = [GMSMarker markerWithPosition:lpUloc.coordinate];

    CLLocationCoordinate2D firstLocation = startMarker.position;
    GMSCoordinateBounds *bounds = [[GMSCoordinateBounds alloc] initWithCoordinate:firstLocation coordinate:firstLocation];
    bounds = [bounds includingCoordinate:lpuMarker.position];
    [mapView_ animateWithCameraUpdate:[GMSCameraUpdate fitBounds:bounds withPadding:50.0f]];

    [self updateZoomSlider];

}

#pragma mark -

-(void)prepareLocationManager{
  //  if (myLocationUpdating) return;
    NSLog(@"PrepareLocMan");
    if (([CLLocationManager authorizationStatus]==kCLAuthorizationStatusAuthorized||[CLLocationManager authorizationStatus]==kCLAuthorizationStatusAuthorizedWhenInUse)&&[CLLocationManager locationServicesEnabled]){
        [locMan startUpdatingLocation];
        [self showActivityIndicatorWithString:@"Определение местоположения"];
        myLocationUpdating = YES;
        [self performSelector:@selector(checkForLocationSearch) withObject:nil afterDelay:kMaximumMyLocationSearchTime];
        NSLog(@"init and start locMan");

    } else {
        NSLog(@"preloadDefault Location from prepare with loc error");

        [self preloadCityForLocation:[self defaultLocation]];
    }
}

-(void)preloadCityForLocation:(CLLocation*) location{
    myCurrentLocation = location;
    [GlobalData loadMyCityByLocation:location fromCashe:NO copml:^(BOOL success, id resultCity) {
        [self removeActivityIdicator];
        if (success){
//            [self showMessage:[NSString stringWithFormat:@"Ваш, или ближайший к Вам город - %@",resultCity] title:@"Вопрос" btns:@[@"Нет", @"Да"] result:^(int index) {
//                [self removeActivityIdicator];
//                searchSpec.text = kSpecAutoSearchLPU;
//                selectedSpec = [NSMutableDictionary dictionaryWithObject:kSpecAutoSearchLPU forKey:@"name"];
//                if (index==0){
//                } else {
//                    selectedCity = [NSMutableDictionary dictionaryWithObject:resultCity forKey:@"name"];
//                    searchCity.text = resultCity;
//                    [self searchData:searchBtnItem];
//                }
//            }];
            [self preloadCity:resultCity];
        }
    }];
    
    // kSpecAutoSearchLPU
    
}

-(CLLocation*)defaultLocation{
    // return Moscow location
    return [[CLLocation alloc] initWithLatitude:55.754816 longitude:37.629748];
}

-(void)preloadCity:(NSString*)cityName{
    NSString *sSpec;
    NSArray *specArr = [GData specializationsTerm:@""];
    if (specArr.count>0){
        sSpec = specArr[0][@"name"];
    } else {
        sSpec = kSpecAutoSearchLPU;
    }
    
    searchSpec.text = sSpec;
    selectedSpec = [NSMutableDictionary dictionaryWithObject:sSpec forKey:@"name"];
    selectedCity = [NSMutableDictionary dictionaryWithObject:cityName forKey:@"name"];
    searchCity.text = cityName;
    searchShowed = NO;
    [self searchData:searchBtnItem];

}

-(IBAction)searchData:(id)sender{
    if (selectedCity==nil){
        [self showMessage:@"Выберите город" title:@"Ошибка"];
        return;
    }
    if (selectedSpec==nil){
        [self showMessage:@"Выберите специализацию" title:@"Ошибка"];
        return;
    }
    
    [UserDefaults setObject:selectedCity[@"name"] forKey:@"lastCity"];
    BOOL isRefresh = sender == self.tableView;
    
    if (isRefresh){

    } else {
        [self showHideSearch:self.showHideSearchBtn];
        [self showActivityIndicatorWithString:@"Загрузка"];
        LPUArray = [NSMutableArray new];
        filtrSpec.text = @"";
        [self scrollCameraToSearchArea:nil];

    }
    
    
    [GData loadLPUSListWithCasheForCity:selectedCity[@"name"] spec:selectedSpec[@"name"] firstCashe:(sender != self.tableView) copml:^(BOOL success, id result){
        if (isRefresh){
            
        } else {
            
        }
        [self removeActivityIdicator];
        [self doneLoadingTableViewData];

        LPUArray = result[@"data"];
        [self.tableView reloadData];
        [self drawMarkers];
        if (LPUArray.count==0&&!isRefresh){
            [self showMessage:@"По Вашему запросу ничего не найдено." title:@"Уведомление" result:^{
                [self showHideSearch:self.showHideSearchBtn];
            }];
        }
        
        [self scrollCameraToSearchArea:nil];
        
    }];
}


-(void)searchCityChangeBlock:(NSString*)text forSender:(UITextFieldAutocompl*)txtField{
    selectedCity = nil;
    NSMutableArray *arr = [GData citiesTerm:text];
   [txtField updateItemsTable:arr];
}

-(void)selectCityBlock:(id)sender{
    selectedCity = searchCity.selectedItem;
}

-(void)searchSpecChangeBlock:(NSString*)text forSender:(UITextFieldAutocompl*)txtField{
    selectedSpec = nil;
    NSMutableArray *arr = [GData specializationsTerm:text];
    [txtField updateItemsTable:arr];
}

-(void)selectSpecBlock:(id)sender{
    selectedSpec = searchSpec.selectedItem;
}

-(void)filtrSpecChangeBlock:(NSString*)text forSender:(UITextFieldAutocompl*)txtField{
    self.selectedLPU = nil;
    
    LPUArray.filtK = @"name";
    LPUArray.filter = filtrSpec.text;
    [LPUArray filtArray];
    
    [txtField updateItemsTable:self.lpuList];

    switch (self.listMapSwitch.selectedSegmentIndex) {
        case 0:{
            [self drawMarkers];
            break;
        }
        case 1:{
            [self.tableView reloadData];
            if (_selectedLPU){
                int index = [_selectedLPU[@"indexPath"] intValue];
                [self.tableView selectRowAtIndexPath:[NSIndexPath indexPathForRow:index inSection:0] animated:NO scrollPosition:UITableViewScrollPositionMiddle];
            }
            break;
        }
    }
    
}

-(void)selectFiltrBlock:(id)sender{
    [self showHideFilter:nil];
}




-(IBAction)selectViewType:(UISegmentedControl*)sender{
    [self selectView:sender.selectedSegmentIndex];
}

-(void)selectView:(int)index{
    switch (index) {
        case 0:{
            filtrSpec.itemsSelector.hidden = NO;
            self.mapContainer.hidden = NO;
            self.tableContainer.hidden = YES;
            [self drawMarkers];
            break;
        }
        case 1:{
            filtrSpec.itemsSelector.hidden = YES;
            self.mapContainer.hidden = YES;
            self.tableContainer.hidden = NO;
            [self.tableView reloadData];
            if (_selectedLPU){
                int index = [_selectedLPU[@"indexPath"] intValue];
                if (!outOfBounds(index, self.lpuList)){
                    [self.tableView selectRowAtIndexPath:[NSIndexPath indexPathForRow:index inSection:0] animated:NO scrollPosition:UITableViewScrollPositionMiddle];
                }
            }

            break;
        }
    }
    
}



-(IBAction)showHideSearch:(UIButton*)sender{
    filterShowed = YES;
    if (!searchShowed){
        [self hideKeyb];
    } else {
        
    }
    
    _filtrFieldsContainer.hidden = YES;
    _searchFiedsContainer.hidden = NO;

    float sy = searchShowed?44:-95;
    float ty = sy + 95;
    float th = self.view.height - ty - 20;
    CGRect tFrame = CGRectMake(self.tableView.x, ty, self.tableView.width, th);
    sender.userInteractionEnabled = NO;
    [UIView animateWithDuration:0.3 animations:^{
        searchContainer.y = sy;
        self.tableView.frame = tFrame;
    } completion:^(BOOL finished) {
        sender.userInteractionEnabled = YES;
        searchShowed = !searchShowed;
    }];

}

-(IBAction)showHideFilter:(UIButton*)sender{
    searchShowed = YES;
    if (!filterShowed){
        [self hideKeyb];
    } else {
        
    }
    _filtrFieldsContainer.hidden = NO;
    _searchFiedsContainer.hidden = YES;

    
    float sy = filterShowed?-50:-95;
    float ty = sy + 95;
    float th = self.view.height - ty - 20;
    CGRect tFrame = CGRectMake(self.tableView.x, ty, self.tableView.width, th);
    sender.userInteractionEnabled = NO;
    [UIView animateWithDuration:0.3 animations:^{
        searchContainer.y = sy;
        self.tableView.frame = tFrame;
    } completion:^(BOOL finished) {
        sender.userInteractionEnabled = YES;
        filterShowed = !filterShowed;
    }];
    
}




-(IBAction)scrollCameraToSearchArea:(id)sender{
    if (self.lpuList.count == 0){
        return;
    }
    
    CLLocationCoordinate2D firstLocation = ((GMSMarker *)gsmMarkersList.firstObject).position;
    GMSCoordinateBounds *bounds = [[GMSCoordinateBounds alloc] initWithCoordinate:firstLocation coordinate:firstLocation];
    
    for (GMSMarker *marker in gsmMarkersList) {
        bounds = [bounds includingCoordinate:marker.position];
    }
    
    [mapView_ animateWithCameraUpdate:[GMSCameraUpdate fitBounds:bounds withPadding:50.0f]];

    [self updateZoomSlider];
}

#pragma mark - Map

-(void)drawMap{

    //camera = [GMSCameraPosition cameraWithLatitude:-33.86 longitude:151.20 zoom:6];
    
    mapView_.settings.myLocationButton = YES;
    mapView_.camera = camera;

    clusterManager_ = [GClusterManager managerWithMapView:mapView_
                                                algorithm:[[NonHierarchicalDistanceBasedAlgorithm alloc] init]
                                                 renderer:[[GDefaultClusterRenderer alloc] initWithMapView:mapView_]];
    
    mapView_.delegate = clusterManager_;

    mapView_.myLocationEnabled = YES;
    
    
    //self.mapContainer = mapView_;
  //  [self.mapContainer addSubview:mapView_];
}

-(void)updateZoomSlider{
    self.zoomSlider.minimumValue = mapView_.camera.zoom;
    self.zoomSlider.maximumValue = 18; //kGMSMaxZoomLevel; //  kGMSMaxZoomLevel = 21
    NSLog(@"setted max = %f, min = %f, current = %f",self.zoomSlider.maximumValue, self.zoomSlider.minimumValue, self.zoomSlider.value);
    self.zoomSlider.value = self.zoomSlider.minimumValue;

}

-(IBAction)zoomSliderChanged:(UISlider*)sender{
    float zoom = sender.value;
    NSLog(@"max = %f, min = %f, current = %f",sender.maximumValue, sender.minimumValue, sender.value);
    if (mapView_.selectedMarker){
        [mapView_ animateToLocation:mapView_.selectedMarker.position];
    }
    [mapView_ animateToZoom:zoom];

}

-(void)drawMarkers{
    [clusterManager_ removeItems];
    gsmMarkersList = [NSMutableArray new];
    GMSMarker *selMarker;
    
   // if (mapView_.selectedMarker) [mapView_ animateToLocation:mapView_.selectedMarker.position];

    for (int i = 0; i<self.lpuList.count; i++){
        
        NSMutableDictionary* place = self.lpuList[i];
        [place setObject:[NSNumber numberWithInt:i] forKey:@"indexPath"];

        double lat = [place[@"lat"] doubleValue];
        double lon = [place[@"lng"] doubleValue];
    
        CLLocation *loc = [[CLLocation alloc] initWithLatitude:lat longitude:lon];
        GMSMarker *marker = [[GMSMarker alloc] init];
        marker.position = CLLocationCoordinate2DMake(loc.coordinate.latitude, loc.coordinate.longitude);
        marker.title = place[@"name"];
        marker.snippet = place[@"address"];
        marker.icon = [UIImage imageNamed:@"gMarker"];
        marker.userData = place;
        if (place == _selectedLPU){
            selMarker = marker;
        }
        // marker.map = mapView_;
        Spot* spot = [self generateSpot:marker];
        [clusterManager_ addItem:spot];
        [gsmMarkersList addObject:marker];
    }
    
    if (mapView_.selectedMarker!=nil){
        [mapView_ setSelectedMarker:selMarker];
        [mapView_ animateToLocation:mapView_.selectedMarker.position];
        //  [mapView_ animateToZoom:14];
       // [clusterManager_ cluster];
    }

    [clusterManager_ cluster];
    
    [clusterManager_ setDelegate:self];
}

- (Spot*)generateSpot:(GMSMarker*)marker {
    Spot* spot = [[Spot alloc] init];
    spot.location = marker.position;
    spot.marker = marker;
    return spot;
}

- (void)mapView:(GMSMapView *)mapView didTapInfoWindowOfMarker:(GMSMarker *)marker {
    [self openLpu:marker.userData];
    NSLog(@"info = %@",marker.userData);
    
}

#pragma mark - GMSMapViewDelegate

- (UIView *)mapView:(GMSMapView *)mapView markerInfoContents:(GMSMarker *)marker {
    
    return nil;

}

- (UIView *)mapView:(GMSMapView *)mapView markerInfoWindow:(GMSMarker *)marker {
    if ([marker.userData hasKey:@"clusterItems"]){
        return nil;
    }
    
    float margin = 10;
    markerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 230, 105)];
    markerView.layer.cornerRadius = 8.0f;
    markerView.clipsToBounds = YES;
    
    UILabel *LPUname = [[UILabel alloc] initWithFrame:CGRectMake(margin, 0, markerView.width-margin*2, 65)];
    LPUname.numberOfLines = 2;
    LPUname.textAlignment = NSTextAlignmentCenter;
    LPUname.font = [UIFont fontWithName:@"Helvetica" size:17.0];
    LPUname.textColor = RGB(53, 55, 58);
    LPUname.text = marker.userData[@"name"];
    [markerView addSubview:LPUname];
    markerView.backgroundColor = [UIColor whiteColor];
    
    UIImageView *separ = [[UIImageView alloc] initWithFrame:CGRectMake(margin, LPUname.bottom, LPUname.width, 1)];
    separ.backgroundColor = RGB(172, 172, 172);
    [markerView addSubview:separ];
    
    ExProButton *detailBtn = [[ExProButton alloc] initWithFrame:CGRectMake(margin, separ.bottom, separ.width, 38)];
    [detailBtn setTitle:@"Подробнее" forState:UIControlStateNormal];
    detailBtn.titleLabel.font = [UIFont fontWithName:@"Helvetica" size:15.0];
    [detailBtn setTitleColor:RGB(53, 55, 58) forState:UIControlStateNormal];
    [detailBtn setImage:[UIImage imageNamed:@"blackRightSmallArrow"] forState:UIControlStateNormal];
    detailBtn.imageEdgeInsets = UIEdgeInsetsMake(2, 154, 0, 0);
    [markerView addSubview:detailBtn];
//    detailBtn.info = marker.userData;
//    [detailBtn addTarget:self action:@selector(openLpuFromMarker:) forControlEvents:UIControlEventTouchUpInside];

//    UIButton *eBtn = [[UIButton alloc] initWithFrame:markerView.bounds];
//    eBtn.backgroundColor = [UIColor redColor];
//    [eBtn addTarget:self action:@selector(openLpuFromMarker:) forControlEvents:UIControlEventTouchUpInside];
//    //[mapView addSubview:eBtn];
//    markerView.userInteractionEnabled = YES;
//    
//    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(openLpuFromMarker:)];
//    tap.enabled = YES;
//    [mapView_ addGestureRecognizer:tap];
//    
    return markerView;

}

-(void)mapView:(GMSMapView *)mapView idleAtCameraPosition:(GMSCameraPosition *)position{
   // NSLog(@"%@",clusterManager_.items);
    self.zoomSlider.value = mapView_.camera.zoom;

}

- (void)mapView:(GMSMapView *)mapView didTapAtCoordinate:(CLLocationCoordinate2D)coordinate {
//    self.calloutView.hidden = YES;
    self.selectedLPU = nil;
}

- (BOOL)mapView:(GMSMapView *)mapView didTapMarker:(GMSMarker *)marker {

    [mapView animateToLocation:marker.position];
    if ([marker.userData hasKey:@"clusterItems"]){
        
        return NO;
    } else {
        mapView.selectedMarker = marker;
        [self changeSelected:marker.userData];
    }
    return YES;
}

-(void)changeSelected:(NSMutableDictionary*)userInfo{
    int index = [userInfo[@"indexPath"] intValue];
    self.self.selectedLPU = userInfo;
    
    switch (self.listMapSwitch.selectedSegmentIndex) {
        case 0:{
            [self.tableView selectRowAtIndexPath:[NSIndexPath indexPathForRow:index inSection:0] animated:NO scrollPosition:UITableViewScrollPositionMiddle];
        //    [self.tableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:index inSection:0] atScrollPosition:UITableViewScrollPositionBottom animated:NO];
        
            break;
        }
        case 1:{
            [mapView_ setSelectedMarker:gsmMarkersList[index]];

            break;
        }
    }
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.lpuList.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 38;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section == 0){
        return 50;
    } else {
        return 0.f;
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"StandartCombyCell";
    
    StandartCombyCell *cell = (StandartCombyCell *)[self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"StandartCombyCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[StandartCombyCell class]])
            {
                cell = (StandartCombyCell *)currentObject;
                break;
            }
        }
    }
    
    NSMutableDictionary *menu = self.lpuList[indexPath.row];
    cell.cellType = ctCaptionSubtitleRightArrow;
    cell.caption.text = menu[@"name"];
    cell.subTitle.text = menu[@"address"];
    
    cell.caption.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];

    cell.backgroundColor = [UIColor whiteColor];
    
    UIView *backViewCell = [[UIView alloc] initWithFrame:cell.bounds];
    backViewCell.backgroundColor = [UIColor lightGrayColor];
    cell.selectedBackgroundView = backViewCell;
    
    if (menu[@"indexRow"]==nil){
        [menu setObject:[NSNumber numberWithInt:indexPath.row] forKey:@"indexPath"];
    }
    
    cell.rightArrow.info = menu;
    [cell.rightArrow addTarget:self action:@selector(openLpuFromTable:) forControlEvents:UIControlEventTouchUpInside];
    cell.rightArrow.userInteractionEnabled = YES;
    
//    if (self.selectedLPU == menu){
//        [cell setSelected:YES animated:NO];
//       // cell.backgroundColor = RGB(200, 200, 200);
//    } else {
//        [cell setSelected:NO animated:NO];
//      //  cell.backgroundColor = [UIColor whiteColor];
//    }

    return cell;
}




#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
   // [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (self.selectedLPU == self.lpuList[indexPath.row]){
        self.selectedLPU = nil;
        [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
        mapView_.selectedMarker = nil;
    } else {
        [self changeSelected:self.lpuList[indexPath.row]];
    }
}

-(void)openLpuFromTable:(ExProButton*)sender{
    [self openLpu:sender.info];
}

- (void)reloadTableViewDataSource{
    //  should be calling your tableviews data source model to reload
    //  put here just for demo
    reloading = YES;
    [self searchData:self.tableView];
}

- (void)doneLoadingTableViewData{
    //  model should call this when its done loading
    reloading = NO;
    [_refreshHeaderView performSelector:@selector(egoRefreshScrollViewDataSourceDidFinishedLoading:) withObject:self.tableView afterDelay:0.3f];
    
    //    [_refreshHeaderView egoRefreshScrollViewDataSourceDidFinishedLoading:self.tableView];
    
}

#pragma mark EGORefreshTableHeaderDelegate Methods

- (void)egoRefreshTableHeaderDidTriggerRefresh:(EGORefreshTableHeaderView*)view{
    [self reloadTableViewDataSource];
    //   [self performSelector:@selector(doneLoadingTableViewData) withObject:nil afterDelay:1.0];
    
}

- (BOOL)egoRefreshTableHeaderDataSourceIsLoading:(EGORefreshTableHeaderView*)view{
    pullTorefreshVisible = YES;
    return reloading; // should return if data source model is reloading
    
}

- (NSDate*)egoRefreshTableHeaderDataSourceLastUpdated:(EGORefreshTableHeaderView*)view{
    pullTorefreshVisible = NO;
    
    return [NSDate date]; // should return date data source was last changed
    
}


#pragma mark -
#pragma mark UIScrollViewDelegate Methods


- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate{
    [_refreshHeaderView egoRefreshScrollViewDidEndDragging:scrollView];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
    [_refreshHeaderView egoRefreshScrollViewDidScroll:scrollView];
    NSLog(@"offst = %f",scrollView.contentOffset.y);
    _refreshHeaderView.alpha = scrollView.contentOffset.y >= 0?0:1;
}

-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
}

-(void)openLpu:(NSMutableDictionary*)lpu{
    if (!lpuDetail){
        lpuDetail = [LPUDetail new];
    }
    lpuDetail.lpu = lpu;
    [self.navigationController pushViewController:lpuDetail animated:YES];
}


@end
