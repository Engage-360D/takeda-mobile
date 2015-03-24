//
//  AnalisisResultPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "AnalisisResultPage.h"
#import "ResultRiskAnal.h"

@interface AnalisisResultPage (){
    NSMutableArray *results;
    BOOL reloading;
    BOOL pullTorefreshVisible;
}

@end

@implementation AnalisisResultPage

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
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if ([User checkForRole:tDoctor]){
        [self initLocalData];
       // [self initData];
    }
}

-(void)setupInterface{
    if (_refreshHeaderView == nil) {
        EGORefreshTableHeaderView *view = [[EGORefreshTableHeaderView alloc] initWithFrame:CGRectMake(0.0f, 0.0f - self.tableView.height, self.tableView.width, self.tableView.height)];
        view.delegate = self;
        _refreshHeaderView = view;
    }
    
    //  update the last update date
    [self.tableView addSubview:_refreshHeaderView];
    [_refreshHeaderView refreshLastUpdatedDate];

    self.tableView.tableFooterView = self.tableView.separ;
}

-(void)initData{
    int lastId = [GlobalData lastResultDataId];
    [GlobalData loadAnalysisFromServerWithLastId:lastId completion:^(BOOL success, NSError *error, id result) {
        NSLog(@"finish loadRisk anal");
        if (success) {
          //  [GlobalData saveResultAnalyses:result[@"data"]];
            [self initLocalData];
        }
        [self doneLoadingTableViewData];
    }];
}

-(void)initLocalData{
    results = [GlobalData resultAnalyses];
    results = [Global recursiveMutable:[results sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
        return [obj2[@"createdAt"] compare:obj1[@"createdAt"]];
    }]];
    
    [self.tableView reloadData];
}

-(IBAction)goToRes:(id)sender{
    ResultRiskAnal *rr = [ResultRiskAnal new];
    [self.navigationController pushViewController:rr animated:YES];
}

-(IBAction)goToResWithInfo:(NSMutableDictionary*)res_data{
    ResultRiskAnal *rr = [ResultRiskAnal new];
    rr.results_data = res_data;
    [self.navigationController pushViewController:rr animated:YES];

}

#pragma mark - Table view data source


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return results.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    return 38;
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
    
    NSMutableDictionary *result = results[indexPath.row];
    cell.cellType = ctLeftCaptionRightArrow;
    cell.caption.text = [[Global parseDateTime:result[@"createdAt"]] stringWithFormat:@"dd MMMM yyyy, HH:mm"];
    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    [self goToResWithInfo:results[indexPath.row]];
}

#pragma mark -

- (void)reloadTableViewDataSource{
    //  should be calling your tableviews data source model to reload
    //  put here just for demo
    reloading = YES;
    [self initData];
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
}

#pragma mark -



@end
