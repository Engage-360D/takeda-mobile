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
        [self initData];
    } else {
        if (self.isFromMenu){
            [self goToRes:nil];
        } else {
            [self openLeftMenu];
        }
    }
}

-(void)setupInterface{
    self.tableView.tableFooterView = self.tableView.separ;
}

-(void)initData{
    results = [GlobalData resultAnalyses];
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




@end
