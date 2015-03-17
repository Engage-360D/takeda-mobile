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



@interface SearchInstitutionPage (){
//    GMSMapView *mapView_;
    GMSCameraPosition *camera;
    GClusterManager *clusterManager_;
    NSMutableArray *lpuList;
    NSMutableArray *gsmMarkersList;

    NSMutableDictionary *selectedCity;
    NSMutableDictionary *selectedSpec;
    BOOL searchShowed;
}

@end

@implementation SearchInstitutionPage
@synthesize mapView_;
@synthesize lpuDetail;
@synthesize searchCity;
@synthesize searchSpec;
@synthesize searchContainer;

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
    [self drawMap];
    [self initData];
    [self performSelector:@selector(scrollToMyLocation) withObject:nil afterDelay:1.0f];

}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    self.container.clipsToBounds = YES;
    UIBarButtonItem *searchBtnItem = [self menuBarBtnWithImageName:@"search_icon" selector:@selector(showHideSearch:) forTarget:self];
    UIBarButtonItem *filterBtnItem = [self menuBarBtnWithImageName:@"filterIcon.png" selector:@selector(showHideSearch:) forTarget:self];
    self.navigationItem.rightBarButtonItems = @[filterBtnItem, searchBtnItem];
    
    
    [self selectView:self.listMapSwitch.selectedSegmentIndex];
    if (!self.isAppearFromBack){
        searchShowed = YES;
        [self showHideSearch:self.showHideSearchBtn];
    }
}

-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
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

    searchCity.backgroundColor = [UIColor colorWithWhite:1 alpha:0.8];
    searchSpec.backgroundColor = [UIColor colorWithWhite:1 alpha:0.8];

    
    self.listMapSwitch.clipsToBounds = YES;
    self.listMapSwitch.layer.cornerRadius = 4.0f;
    
    searchCity.leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, searchCity.height)];
    searchCity.leftViewMode = UITextFieldViewModeAlways;
    
    searchSpec.leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, searchSpec.height)];
    searchSpec.leftViewMode = UITextFieldViewModeAlways;
  
    searchCity.layer.cornerRadius = 5.0f;
    searchSpec.layer.cornerRadius = 5.0f;

    [searchCity setPlaceholderColor:RGB(53, 65, 71)];
    [searchSpec setPlaceholderColor:RGB(53, 65, 71)];

    searchCity.itemsKey = @"name";
    searchSpec.itemsKey = @"name";
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

-(IBAction)selectViewType:(UISegmentedControl*)sender{
    [self selectView:sender.selectedSegmentIndex];
}

-(void)selectView:(int)index{
    switch (index) {
        case 0:{
            self.mapContainer.hidden = NO;
            self.tableContainer.hidden = YES;
            [self drawMarkers];
            break;
        }
        case 1:{
            self.mapContainer.hidden = YES;
            self.tableContainer.hidden = NO;
            [self.tableView reloadData];
            break;
        }
    }
}



-(IBAction)showHideSearch:(UIButton*)sender{
   
//    CGFloat y;
//    CGFloat h;
//    
//    if (searchShowed){
//        _searchView.hidden = NO;
//        y = _searchView.bottom;
//    } else {
//        y = 0;
//    }
//    
//    h = self.view.height - y - (isKeyb?kKeyboardHeight:0);
    if (!searchShowed){
        [self hideKeyb];
    }
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


-(void)initData{
    // name
    // adres
    // location
    // category
    // short description
    
    

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
    
    [self showHideSearch:self.showHideSearchBtn];
    lpuList = [NSMutableArray new];
    [self scrollCameraToSearchArea];

    [self showActivityIndicatorWithString:@"Загрузка"];
    [GData loadLPUSListForCity:selectedCity[@"name"] spec:selectedSpec[@"name"] copml:^(BOOL success, id result){
        [self removeActivityIdicator];
        lpuList = result[@"data"];
        [self.tableView reloadData];
        [self drawMarkers];
        if (lpuList.count==0){
            [self showMessage:@"По Вашему запросу ничего не найдено." title:@"Уведомление" result:^{
                [self showHideSearch:self.showHideSearchBtn];
            }];
        }

        [self scrollCameraToSearchArea];

    }];
}

-(void)scrollCameraToSearchArea{
    if (lpuList.count == 0){
        return;
    }
    
    CLLocationCoordinate2D firstLocation = ((GMSMarker *)gsmMarkersList.firstObject).position;
    GMSCoordinateBounds *bounds = [[GMSCoordinateBounds alloc] initWithCoordinate:firstLocation coordinate:firstLocation];
    
    for (GMSMarker *marker in gsmMarkersList) {
        bounds = [bounds includingCoordinate:marker.position];
    }
    
    [mapView_ animateWithCameraUpdate:[GMSCameraUpdate fitBounds:bounds withPadding:50.0f]];

}

#pragma mark - Map

-(void)drawMap{
    
    camera = [GMSCameraPosition cameraWithLatitude:-33.86 longitude:151.20 zoom:6];
    
    mapView_.settings.myLocationButton = YES;
    mapView_.camera = camera;

    clusterManager_ = [GClusterManager managerWithMapView:mapView_
                                                algorithm:[[NonHierarchicalDistanceBasedAlgorithm alloc] init]
                                                 renderer:[[GDefaultClusterRenderer alloc] initWithMapView:mapView_]];
    
    mapView_.delegate = clusterManager_;

    mapView_.myLocationEnabled = YES;
    //self.mapContainer = mapView_;
    [self.mapContainer addSubview:mapView_];
}

-(void)drawMarkers{
    [clusterManager_ removeItems];
    gsmMarkersList = [NSMutableArray new];
    for (NSMutableDictionary* place in lpuList){
        
        double lat = [place[@"lat"] doubleValue];
        double lon = [place[@"lng"] doubleValue];
    
        CLLocation *loc = [[CLLocation alloc] initWithLatitude:lat longitude:lon];
        GMSMarker *marker = [[GMSMarker alloc] init];
        marker.position = CLLocationCoordinate2DMake(loc.coordinate.latitude, loc.coordinate.longitude);
        marker.title = place[@"name"];
        marker.snippet = place[@"address"];
        // marker.map = mapView_;
        Spot* spot = [self generateSpot:marker];
        [clusterManager_ addItem:spot];
        [gsmMarkersList addObject:marker];
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

- (BOOL)mapView:(GMSMapView *)mapView didTapMarker:(GMSMarker *)marker {
    [[[UIAlertView alloc] initWithTitle:@"DidTapMarker" message:marker.title delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    return YES;
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return lpuList.count;
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
    
    NSMutableDictionary *menu = lpuList[indexPath.row];
    cell.cellType = ctCaptionSubtitleRightArrow;
    cell.caption.text = menu[@"name"];
    cell.subTitle.text = menu[@"address"];
    
    cell.caption.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];

    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}




#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    [self openLpu:lpuList[indexPath.row]];
}

-(void)openLpu:(NSMutableDictionary*)lpu{
    if (!lpuDetail){
        lpuDetail = [LPUDetail new];
    }
    lpuDetail.lpu = lpu;
    [self.navigationController pushViewController:lpuDetail animated:YES];
}


@end
