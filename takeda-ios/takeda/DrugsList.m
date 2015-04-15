//
//  DrugsList.m
//  takeda
//
//  Created by Alexander Rudenko on 25.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "DrugsList.h"

@interface DrugsList (){
    BOOL reloading;
    BOOL pullTorefreshVisible;
}

@end

@implementation DrugsList
@synthesize drugs;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupInterface];
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

    self.navigationItem.rightBarButtonItems = nil;
    self.navigationItem.rightBarButtonItems = @[[self menuBarBtnWithImageName:@"addWhiteInCircle" selector:@selector(addPillsAction) forTarget:self],[self alarmButton]];
    self.tableView.tableFooterView = self.tableView.separ;
    
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
}

-(void)initData{
    [GlobalData loadPillsCompletition:^(BOOL success, id result){
        drugs = [GlobalData pills];
        [self doneLoadingTableViewData];

        [self.tableView reloadData];
    }];
}


#pragma mark - Table view data source


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return drugs.count;
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
    
    NSMutableDictionary *drug = drugs[indexPath.row];
    cell.cellType = ctCaptionSubtitleRightArrow;
    cell.caption.text = drug[@"name"];
    
    NSDate *dTime = [Global parseTime:drug[@"time"]];
    NSString *sbt = [Global dictionaryWithValue:drug[@"repeat"] ForKey:@"name" InArray:[AddPills expArray]][@"title"];
    cell.subTitle.text = [NSString stringWithFormat:@"%@ ( %@ шт. %@)",[dTime stringWithFormat:@"HH:mm"],drug[@"quantity"], sbt];

    cell.backgroundColor = [UIColor whiteColor];
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    [self goToDrug:drugs[indexPath.row]];
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


-(void)goToDrug:(NSMutableDictionary*)drug{
    _addPills = [AddPills new];
    _addPills.drug = drug;
    _addPills.readOnly = YES;
    _addPills.preview = YES;
    [self.navigationController pushViewController:_addPills animated:YES];
}

-(void)addPillsAction{
    _addPills = [AddPills new];
    [self.navigationController pushViewController:_addPills animated:YES];
}

@end
